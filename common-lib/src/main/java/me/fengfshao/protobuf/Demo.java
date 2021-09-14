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
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.Message;
import io.protostuff.compiler.parser.ClasspathFileReader;
import io.protostuff.compiler.parser.Importer;
import io.protostuff.compiler.parser.ProtoContext;
import java.util.Arrays;
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
                .build();

        schemaBuilder.addMessageDefinition(msgDef);
        DynamicSchema schema = schemaBuilder.build();
        //DynamicSchema schema = DynamicSchema.parseFrom(new FileInputStream("/Users/sakura1/stuff/codelib/common-lib/src/main/protobuf/person.proto"));

// Create dynamic message from schema
        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Person");
        Descriptor msgDesc = msgBuilder.getDescriptorForType();
        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("id"), 1)
                .setField(msgDesc.findFieldByName("name"), "jihite")
                .setField(msgDesc.findFieldByName("email"), "jihite@jihite.com")
                .build();

        byte[] bytes1 = msg.toByteArray();

        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1);
        builder.setName("jihite");
        builder.setEmail("jihite@jihite.com");
        byte[] bytes2 = builder.build().toByteArray();

        System.out.println(Arrays.equals(bytes1, bytes2));
    }


    public static void parseFromProtoFile(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new ParserModule());
        Importer importer = injector.getInstance(Importer.class);
        ProtoContext context = importer.importFile(
                new ClasspathFileReader(), "person.proto");

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
                .setField(msgDesc.findFieldByName("id"), 1)
                .setField(msgDesc.findFieldByName("name"), "jihite")
                .setField(msgDesc.findFieldByName("email"), "jihite@jihite.com")
                .build();

        byte[] bytes1 = msg.toByteArray();


        PersonProto.Person p2 = PersonProto.Person.parseFrom(bytes1);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());

    }


}
