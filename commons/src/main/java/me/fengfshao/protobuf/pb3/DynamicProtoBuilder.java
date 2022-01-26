package me.fengfshao.protobuf.pb3;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.EnumDefinition;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.github.os72.protobuf.dynamic.MessageDefinition.Builder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.DynamicMessage;
import io.protostuff.compiler.ParserModule;
import io.protostuff.compiler.model.Enum;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.Message;
import io.protostuff.compiler.parser.FileReader;
import io.protostuff.compiler.parser.Importer;
import io.protostuff.compiler.parser.ProtoContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: fengfshao
 * Date: 2021/10/12 10:48
 * Package: me.fengfshao.protobuf.pb3
 * Description:
 *
 * 动态构建proto协议中message对象，无需protoc编译
 * 目前支持扁平结构proto协议，如
 *
 * ================================
 * message Person {
 * int32 id = 1;
 * string name = 2;
 * string email = 3;
 * repeated string address = 4;
 * }
 * ================================
 *
 * 参考自：
 *  https://stackoverflow.com/questions/18836727/protocol-buffer-objects-generated-at-runtime
 *  https://stackoverflow.com/questions/47635038/protocol-buffers-how-to-parse-a-proto-file-in-java
 *  https://www.quora.com/Protocol-Buffers-Has-anyone-implemented-a-parser-for-proto-files-in-Java
 *  https://github.com/protostuff/protostuff-compiler
 */
public class DynamicProtoBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicProtoBuilder.class);

    private static class InputStreamReader implements FileReader {

        private final InputStream in;

        public InputStreamReader(InputStream in) {
            this.in = in;
        }

        @Override
        public CharStream read(String name) {
            try {
                return CharStreams.fromStream(in);
            } catch (Exception e) {
                LOGGER.error("Could not read {}", name, e);
            }
            return null;
        }
    }

    public static class ProtoHolder {

        public static final ConcurrentHashMap<String, DynamicSchema> cache = new ConcurrentHashMap<>();

        public static void registerOrUpdate(byte[] protoBytes, String protoFileName)
                throws Exception {
            InputStream protoInputStream = new ByteArrayInputStream(protoBytes);
            DynamicSchema schema = parseProtoFile(protoInputStream);
            cache.put(protoFileName, schema);
        }

        public static void registerOrUpdate(InputStream protoInputStream, String protoFileName)
                throws Exception {
            DynamicSchema schema = parseProtoFile(protoInputStream);
            cache.put(protoFileName, schema);
        }
    }


    // 字段类型仅支持scalar,num,Message类型
    private static DynamicSchema parseProtoFile(InputStream protoInputStream) throws Exception {
        Injector injector = Guice.createInjector(new ParserModule());
        Importer importer = injector.getInstance(Importer.class);

        ProtoContext context = importer.importFile(
                new InputStreamReader(protoInputStream), null);

        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();

        context.getProto().getMessages().forEach(e -> {
            MessageDefinition msgDef = createMessageDefinition(e);
            schemaBuilder.addMessageDefinition(msgDef);
        });

        context.getProto().getEnums().forEach(e -> {
            EnumDefinition enumDef = createEnumDefinition(e);
            schemaBuilder.addEnumDefinition(enumDef);
        });
        protoInputStream.close();
        return schemaBuilder.build();
    }


    /*
     * 按照深度优先顺序，构造嵌套的Message定义*/
    private static MessageDefinition createMessageDefinition(Message message) {
        Builder builder = MessageDefinition.newBuilder(message.getName());
        for (Field f : message.getFields()) {
            if (!f.getType().isScalar() && !f.getType().isMessage()) {
                throw new UnsupportedOperationException("unsupported field type in proto.");
            }
            String label = f.isRepeated() ? "repeated" : "optional";
            builder.addField(label, f.getType().getName(), f.getName(), f.getIndex());
        }

        for (Message nestedMessage : message.getMessages()) {
            MessageDefinition nestedMsgDef = createMessageDefinition(nestedMessage);
            builder.addMessageDefinition(nestedMsgDef);
        }

        for (Enum e : message.getEnums()) {
            EnumDefinition enumDef = createEnumDefinition(e);
            builder.addEnumDefinition(enumDef);
        }

        return builder.build();
    }

    private static EnumDefinition createEnumDefinition(Enum e) {
        EnumDefinition.Builder builder = EnumDefinition.newBuilder(e.getName());
        e.getConstants().forEach(c -> {
            builder.addValue(c.getName(), c.getValue());
        });
        return builder.build();
    }


    private static void setField(com.google.protobuf.Message.Builder builder, String field, Object value) {
        if (null != value) {
            FieldDescriptor descriptor = builder.getDescriptorForType()
                    .findFieldByName(field);
            if (descriptor.getJavaType().equals(JavaType.ENUM)) {
                String enumValue = String.valueOf(value);
                builder.setField(descriptor,
                        descriptor.getEnumType().findValueByName(enumValue));
            } else if (descriptor.isRepeated()) {
                assert value instanceof List;
                if (value instanceof List) {
                    List<String> values = (List<String>) value;
                    for (String s : values) {
                        Object result = getObject(s, field, descriptor.getJavaType());
                        if (null != result) {
                            builder.addRepeatedField(descriptor, result);
                        }
                    }
                }
            } else {
                String str = String.valueOf(value);
                Object result = getObject(str, field, descriptor.getJavaType());

                if (null != result) {
                    builder.setField(descriptor, result);
                }
                //builder.setField(field, EnumValueDescriptor)
            }
        }
    }

    /**
     * 根据pb相应field的type，转换pb value
     *
     * @param str 待转换的pb value
     * @param type pb相应field的type
     * @return 转换后的pb value
     */
    private static Object getObject(String str, String field,
            FieldDescriptor.JavaType type) {
        if (null != str && str.trim().length() > 0) {
            try {
                switch (type) {
                    case INT:
                        return Integer.parseInt(str);
                    case LONG:
                        return Long.parseLong(str);
                    case FLOAT:
                        return Float.parseFloat(str);
                    case DOUBLE:
                        return Double.parseDouble(str);
                    case BOOLEAN:
                        return Boolean.parseBoolean(str);
                    case STRING:
                    case ENUM:
                        return str;
                    default:
                        // BYTE_STRING, ENUM, MESSAGE
                        return null;
                }
            } catch (Exception e) {
                LOGGER.warn("PbUtil convert error! key: " + field + ", value: " + str, e);
            }
        }
        return null;
    }


    /**
     * @param messageName proto文件中的message name
     * @param fieldValues 填充的字段值，repeated字段需要为List类型
     * @return
     */
    public static DynamicMessage buildMessage(String protoName, String messageName,
            Map<String, Object> fieldValues) {
        DynamicMessage.Builder msgBuilder = ProtoHolder.cache.get(protoName)
                .newMessageBuilder(messageName);
        Descriptor msgDesc = msgBuilder.getDescriptorForType();

        List<FieldDescriptor> fdList = msgDesc.getFields();

        fdList.forEach(fd -> {
            String fdName = fd.getName();
            if (fd.getJavaType().equals(JavaType.MESSAGE)) {
                Map<String, Object> nestedField = (Map<String, Object>) fieldValues.get(fdName);
                DynamicMessage currMsg = buildMessage(protoName, fd.getMessageType().getFullName(), nestedField);
                msgBuilder.setField(msgDesc.findFieldByName(fdName), currMsg);
            } else {
                setField(msgBuilder, fdName, fieldValues.get(fdName));
            }
        });
        return msgBuilder.build();
    }

    public static void main1(String[] args) throws Exception {
        InputStream protoInputStream = Thread.currentThread().getContextClassLoader()
                .getResource("person.proto").openStream();
        //ProtoHolder.registerProto(protoInputStream,"Person");
        //ProtoHolder.registerProto(protoInputStream, Arrays.asList("Person", "Dog"));
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("id", 1);
        fieldValues.put("name", "jihite");
        fieldValues.put("address", Arrays.asList("NewYork", "Chicago"));

        /*DynamicMessage msg = DynamicProtoBuilderV2.buildMessage("","Person", fieldValues);
        byte[] bytes = msg.toByteArray();

        PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes);
        System.out.println(p2);

        Map<String, Object> fieldValues2 = new HashMap<>();
        fieldValues2.put("name", "lee");
        fieldValues2.put("type", "SHIBINU");
        DynamicMessage msg2 = DynamicProtoBuilderV2.buildMessage("","Dog", fieldValues2);
        System.out.println(msg2);*/
    }

    public static void main(String[] args) throws Exception {
        InputStream personProto = Thread.currentThread().getContextClassLoader()
                .getResource("person.proto").openStream();
        InputStream peopleProto = Thread.currentThread().getContextClassLoader()
                .getResource("people.proto").openStream();
        ProtoHolder.registerOrUpdate(personProto, "person.proto");
        ProtoHolder.registerOrUpdate(peopleProto, "people.proto");

        Map<String, Object> fieldValuesPerson = new HashMap<>();
        fieldValuesPerson.put("id", 1);
        fieldValuesPerson.put("name", "jihite");
        fieldValuesPerson.put("email", "jihite@jihite.com");

        Map<String, Object> fieldValuesDog = new HashMap<>();
        fieldValuesDog.put("name", "xiaobai");
        fieldValuesDog.put("type", "SHIBINU");
        fieldValuesDog.put("gender", "FEMALE");

        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("address", "xxx-aaa-bbb");
        fieldValues.put("family_name", "shao");
        fieldValues.put("owner", fieldValuesPerson);
        fieldValues.put("watcher", fieldValuesDog);

        DynamicMessage msg = buildMessage("people.proto","House", fieldValues);
        System.out.println(msg);

        PersonProto.House p2 = PersonProto.House.parseFrom(msg.toByteArray());

        System.out.println(p2);

        /*ProtoHolder.registerProto(protoInputStream,Arrays.asList("Person","Dog"));
        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("id", 1);
        fieldValues.put("name", "jihite");
        fieldValues.put("address", Arrays.asList("NewYork","Chicago"));

        DynamicMessage msg=DynamicProtoBuilder.buildMessage("Person", fieldValues);
        byte[] bytes = msg.toByteArray();

        PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes);
        System.out.println(p2);

        Map<String, Object> fieldValues2 = new HashMap<>();
        fieldValues2.put("name", "lee");
        fieldValues2.put("type", "SHIBINU");
        DynamicMessage msg2=DynamicProtoBuilder.buildMessage("Dog", fieldValues2);
        System.out.println(msg2);*/
    }

}
