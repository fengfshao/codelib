package me.fengfshao.protobuf.pb3;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.github.os72.protobuf.dynamic.MessageDefinition.Builder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import io.protostuff.compiler.ParserModule;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.Message;
import io.protostuff.compiler.parser.FileReader;
import io.protostuff.compiler.parser.Importer;
import io.protostuff.compiler.parser.ProtoContext;
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
 *   int32 id = 1;
 *   string name = 2;
 *   string email = 3;
 *   repeated string address = 4;
 * }
 * ================================
 */
public class DynamicProtoBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicProtoBuilder.class);
    private static final Injector injector = Guice.createInjector(new ParserModule());
    private static final Importer importer = injector.getInstance(Importer.class);

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

        private static final ConcurrentHashMap<String, DynamicSchema> cache = new ConcurrentHashMap<>();

        public static void registerProto(InputStream inputStream, String messageName, boolean update) throws Exception {
            DynamicSchema schema = parseProtoFile(inputStream, messageName);
            if (update) {
                cache.put(messageName, schema);
            } else {
                cache.putIfAbsent(messageName, schema);
            }
        }

        public static void registerProto(InputStream inputStream, String messageName) throws Exception {
            registerProto(inputStream, messageName, true);
        }
    }


    private static DynamicSchema parseProtoFile(InputStream input, String messageName) throws Exception {
        ProtoContext context = importer.importFile(new InputStreamReader(input), null);
        Message m = context.resolve(".." + messageName, Message.class);
        Builder builder = MessageDefinition.newBuilder(messageName);

        for (Field f : m.getFields()) {
            if(f.isRepeated()){
                builder.addField("repeated", f.getType().toString(), f.getName(), f.getIndex());
            }else{
                builder.addField("optional", f.getType().toString(), f.getName(), f.getIndex());
            }
        }
        MessageDefinition msgDef = builder.build();
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
        schemaBuilder.addMessageDefinition(msgDef);
        return schemaBuilder.build();
    }


    private static void setField(com.google.protobuf.Message.Builder builder, String field, Object value) {
        if (null != value) {
            Descriptors.FieldDescriptor descriptor = builder.getDescriptorForType()
                    .findFieldByName(field);
            if (descriptor.isRepeated()) {
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
                String str = String.valueOf(value) ;
                Object result = getObject(str, field, descriptor.getJavaType());
                if (null != result) {
                    builder.setField(descriptor, result);
                }
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
            Descriptors.FieldDescriptor.JavaType type) {
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
    public static DynamicMessage buildMessage(String messageName, Map<String, Object> fieldValues) {
        if (!ProtoHolder.cache.containsKey(messageName)) {
            throw new RuntimeException("remember use ProtoHolder#addProto before build pb message.");
        }
        DynamicSchema schema = ProtoHolder.cache.get(messageName);
        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder(messageName);
        List<FieldDescriptor> fdList = msgBuilder.getDescriptorForType().getFields();
        fdList.forEach(fd -> {
            Object value = fieldValues.get(fd.getName());
            if (value != null) {
                setField(msgBuilder, fd.getName(), value);
            }
        });
        return msgBuilder.build();
    }

    public static void main(String[] args) throws Exception {
        InputStream protoInputStream = Thread.currentThread().getContextClassLoader()
                .getResource("person.proto").openStream();
        ProtoHolder.registerProto(protoInputStream,"Person");

        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("id", 1);
        fieldValues.put("name", "jihite");
        fieldValues.put("address", Arrays.asList("NewYork","Chicago"));

        DynamicMessage msg=DynamicProtoBuilder.buildMessage("Person", fieldValues);
        byte[] bytes = msg.toByteArray();

        PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());
        System.out.println("after address:" + p2.getAddressList());



    }

}
