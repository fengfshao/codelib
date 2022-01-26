package me.fengfshao.protobuf;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.github.os72.protobuf.dynamic.MessageDefinition.Builder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import io.protostuff.compiler.ParserModule;
import io.protostuff.compiler.model.Enum;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.Message;
import io.protostuff.compiler.parser.ClasspathFileReader;
import io.protostuff.compiler.parser.Importer;
import io.protostuff.compiler.parser.ProtoContext;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.fengfshao.protobuf.pb3.DynamicProtoBuilder;
import me.fengfshao.protobuf.pb3.DynamicProtoBuilder.ProtoHolder;
import me.fengfshao.protobuf.pb3.PeopleProto;
import me.fengfshao.protobuf.pb3.PersonProto;

/**
 * Author: fengfshao
 * Date: 2021/9/14 17:30
 * Package: me.fengfshao.protobuf
 * Description:
 *
 * 参考 https://stackoverflow.com/questions/18836727/protocol-buffer-objects-generated-at-runtime
 * https://stackoverflow.com/questions/47635038/protocol-buffers-how-to-parse-a-proto-file-in-java
 * https://www.quora.com/Protocol-Buffers-Has-anyone-implemented-a-parser-for-proto-files-in-Java
 * https://github.com/protostuff/protostuff-compiler
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        parseFromProtoFile(args);
    }

    public static void buildAndDebuild(String[] args) throws InvalidProtocolBufferException {
        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1);
        builder.setName("jihite");
        builder.setEmail("jihite@jihite.com");

        PersonProto.Person person = builder.build();
        System.out.println("before:" + person);

        System.out.println("===Person Byte:");
        for (byte b : person.toByteArray()) {
            System.out.print(b);
        }
        System.out.println("================");

        byte[] byteArray = person.toByteArray();
        PersonProto.Person p2 = PersonProto.Person.parseFrom(byteArray);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());
    }

    public static void dynamicBuildDemo(String[] args)
            throws Exception {
// Create dynamic schema
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
        schemaBuilder.setName("PersonSchemaDynamic.proto");

        MessageDefinition msgDef = MessageDefinition.newBuilder("Person") // message Person
                .addField("optional", "int32", "id", 1)     // required int32 id = 1
                .addField("optional", "string", "name", 2)  // required string name = 2
                .addField("optional", "string", "email", 3) // optional string email = 3
                .addField("repeated", "string", "address", 4) // optional string email = 3
                .build();

        schemaBuilder.addMessageDefinition(msgDef);
        DynamicSchema schema = schemaBuilder.build();
        //DynamicSchema schema = DynamicSchema.parseFrom(new FileInputStream("/Users/sakura1/stuff/codelib/common-lib/src/main/protobuf/person.proto"));

// Create dynamic message from schema
        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Person");
        Descriptor msgDesc = msgBuilder.getDescriptorForType();
        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("id"), 1)
                .setField(msgDesc.findFieldByName("email"), "jihite@jihite.com")
                .setField(msgDesc.findFieldByName("address"), Arrays.asList("aaa","bbb"))
                .build();

        byte[] bytes1 = msg.toByteArray();

        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1);
        builder.setEmail("jihite@jihite.com");
        builder.addAllAddress(Arrays.asList("aaa", "bbb"));
        byte[] bytes2 = builder.build().toByteArray();

        InputStream protoInputStream = Thread.currentThread().getContextClassLoader()
                .getResource("person.proto").openStream();
        ProtoHolder.registerOrUpdate(protoInputStream,"Person");

        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("id", 1);
        fieldValues.put("email", "jihite@jihite.com");
        fieldValues.put("address", Arrays.asList("aaa","bbb"));


        DynamicMessage msg3= DynamicProtoBuilder.buildMessage("people.proto","Person", fieldValues);
        byte[] bytes3 = msg3.toByteArray();

        System.out.println(Arrays.equals(bytes1, bytes2));
        System.out.println(Arrays.equals(bytes2, bytes3));

    }


    public static void parseFromProtoFile(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new ParserModule());
        Importer importer = injector.getInstance(Importer.class);
        ProtoContext context = importer.importFile(
                new ClasspathFileReader(), "person.proto");

        List<Enum> enums = context.getProto().getEnums();
        enums.forEach(e->{
            System.out.println(e.getFullyQualifiedName());
        });
        Message t = context.resolve("..Person", Message.class);
        Builder builder = MessageDefinition.newBuilder("Person");

        for (Field f : t.getFields()) {
            builder.addField("optional", f.getType().toString(), f.getName(), f.getIndex());
        }
        MessageDefinition msgDef = builder.build();
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
        schemaBuilder.addMessageDefinition(msgDef);
        DynamicSchema schema = schemaBuilder.build();

        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Person");
        Descriptor msgDesc = msgBuilder.getDescriptorForType();
        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("id"), "1")
                .setField(msgDesc.findFieldByName("name"), "jihite")
                .setField(msgDesc.findFieldByName("email"), "jihite@jihite.com")
                .build();

        byte[] bytes1 = msg.toByteArray();


        PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes1);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());

    }

    public static void dynamicBuildNestedDemo(String[] args)
            throws Exception {
        // Create dynamic schema
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();

        MessageDefinition msgDef1 = MessageDefinition.newBuilder("Person") // message Person
                .addField("optional", "int32", "id", 1)     // required int32 id = 1
                .addField("optional", "string", "name", 2)  // required string name = 2
                .addField("optional", "string", "email", 3) // optional string email = 3
                .addField("repeated", "string", "address", 4) // optional string email = 3
                .build();

        MessageDefinition msgDef2 = MessageDefinition.newBuilder("Dog") // message Person
                .addField("optional", "string", "name", 1)  // required string name = 2
                .addField("optional", "string", "type", 2) // optional string email = 3
                .build();

        MessageDefinition msgDef3 = MessageDefinition.newBuilder("House") // message Person
                .addField("optional", "string", "address", 1)  // required string name = 2
                .addField("optional", "string", "family_name", 2) // optional string email = 3
                .addField("optional", "Person", "owner", 3) // optional string email = 3
                .addField("optional", "Dog", "watcher", 4) // optional string email = 3
                .build();

        schemaBuilder.addMessageDefinition(msgDef1);
        schemaBuilder.addMessageDefinition(msgDef2);
        schemaBuilder.addMessageDefinition(msgDef3);
        // DynamicSchema schema = schemaBuilder.build();
        InputStream protoInputStream = Thread.currentThread().getContextClassLoader()
                .getResource("people.proto").openStream();
        DynamicProtoBuilder.ProtoHolder.registerOrUpdate(protoInputStream, "people.proto");
        DynamicSchema schema= DynamicProtoBuilder.ProtoHolder.cache.get("people.proto");



        DynamicMessage.Builder msgBuilderPerson = schema.newMessageBuilder("House.Person");
        Descriptor msgDescPerson = msgBuilderPerson.getDescriptorForType();

        DynamicMessage msgPerson = msgBuilderPerson
                .setField(msgDescPerson.findFieldByName("id"), 1)
                .setField(msgDescPerson.findFieldByName("name"), "jihite")
                .setField(msgDescPerson.findFieldByName("email"), "jihite@jihite.com")
                .build();

        DynamicMessage.Builder msgBuilderDog = schema.newMessageBuilder("House.Dog");
        Descriptor msgDescDog = msgBuilderDog.getDescriptorForType();

        DynamicMessage msgDog = msgBuilderDog
                .setField(msgDescDog.findFieldByName("name"), "xiaobai")
                .setField(msgDescDog.findFieldByName("type"), "SHIBINU")
                .build();

        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("House");
        Descriptor msgDesc = msgBuilder.getDescriptorForType();

        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("address"), "xxx-aaa-bbb")
                .setField(msgDesc.findFieldByName("family_name"), "shao")
                .setField(msgDesc.findFieldByName("owner"), msgPerson)
                .setField(msgDesc.findFieldByName("watcher"), msgDog)
                .build();

        System.out.println(msg);

        PeopleProto.House h= PeopleProto.House.parseFrom(msg.toByteArray());
        System.out.println(h);
        /*byte[] bytes1 = msg.toByteArray();

        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1);
        builder.setEmail("jihite@jihite.com");
        builder.addAllAddress(Arrays.asList("aaa", "bbb"));
        byte[] bytes2 = builder.build().toByteArray();

        InputStream protoInputStream = Thread.currentThread().getContextClassLoader()
                .getResource("person.proto").openStream();
        ProtoHolder.registerProto(protoInputStream,"Person");

        Map<String, Object> fieldValues = new HashMap<>();
        fieldValues.put("id", 1);
        fieldValues.put("email", "jihite@jihite.com");
        fieldValues.put("address", Arrays.asList("aaa","bbb"));


        DynamicMessage msg3= DynamicProtoBuilder.buildMessage("Person", fieldValues);
        byte[] bytes3 = msg3.toByteArray();
*/
  //      System.out.println(Arrays.equals(bytes1, bytes2));
   //     System.out.println(Arrays.equals(bytes2, bytes3));

    }

    public static void parseFromProtoFileNested(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new ParserModule());
        Importer importer = injector.getInstance(Importer.class);
        ProtoContext context = importer.importFile(
                new ClasspathFileReader(), "people.proto");

        Message t = context.resolve("..House", Message.class);
        List<Message> list= context.getProto().getMessages();
        System.out.println(list);
        Builder builder = MessageDefinition.newBuilder("House");
        for (Field f : t.getFields()) {
            if(!f.getType().isScalar()&&!f.getType().isMessage()){
                throw new UnsupportedOperationException("unsupported field type in proto.");
            }
            if(f.getType().isScalar()){
                builder.addField("optional", f.getType().toString(), f.getName(), f.getIndex());
                System.out.println("Scala: "+f.getName());
            }else {
                //System.out.println(t.getMessages());
                Message cur = t.getMessage(f.getType().getName());

                System.out.println(cur.isNested());
                //System.out.println(cur);
                Builder curBuilder = MessageDefinition.newBuilder(f.getType().getName());

                for (Field f1 : cur.getFields()) {
                    curBuilder.addField("optional", f1.getType().getName(), f1.getName(), f1.getIndex());
                }
                MessageDefinition tmp=curBuilder.build();
                //System.out.println(tmp);
                builder.addMessageDefinition(tmp);
                builder.addField("optional", f.getType().getName(), f.getName(), f.getIndex());
                System.out.println("nonScala: "+f.getName());
            }
            //builder.addField("optional", f.getType().toString(), f.getName(), f.getIndex());
        }
        MessageDefinition msgDef = builder.build();
        System.out.println(msgDef);
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
        schemaBuilder.addMessageDefinition(msgDef);
        DynamicSchema schema = schemaBuilder.build();

        /*DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Person");
        Descriptor msgDesc = msgBuilder.getDescriptorForType();
        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("id"), 1)
                .setField(msgDesc.findFieldByName("name"), "jihite")
                .setField(msgDesc.findFieldByName("email"), "jihite@jihite.com")
                .build();*/

//        byte[] bytes1 = msg.toByteArray();


        /*PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes1);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());*/

    }


}
