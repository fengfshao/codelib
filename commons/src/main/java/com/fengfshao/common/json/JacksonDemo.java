package com.fengfshao.common.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用jackson进行json序列化和反序列化
 *
 * @author fengfshao
 */
public class JacksonDemo {

    private static class Student {

        String name;
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

/*        public String getSomething() {
            return "sm";
        }*/

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    /**
     * 使用高阶api解析json并基于类型进行反序列化
     * <a href="https://cloud.tencent.com/developer/article/1447092">关于泛型擦出和获取</a>
     */
    public static void HighApiParse() throws Exception {
        String json1 = getJson(Collections.singletonMap("personal", Collections.singletonMap("name", "wang")));
        System.out.println(json1);
        ObjectMapper mapper = new ObjectMapper();

        // 借助java的泛型机制是泛型类中的泛型不会被擦除（还有成员变量的参数等）
        // 的特性，达到实现传递具体泛型参数的目的。
        Map<String, Object> map = mapper.readValue(json1, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map);
    }

    /**
     * 关于jackson反序列化时，自动基于json类型映射至对应的java类型，容器中的泛型类型同
     */
    public static void AutoTypeMapping() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // number-> int/long/BigInteger
        System.out.println(mapper.readValue("123", Object.class).getClass());
        System.out.println(mapper.readValue("12345678910", Object.class).getClass());
        System.out.println(mapper.readValue("1234567891012345678910", Object.class).getClass());
        String json1 = getJson(Collections.singletonMap("personal", Collections.singletonMap("name", "wang")));
        // string -> String
        System.out.println(mapper.readValue("\"hello\"", Object.class).getClass());
        // object -> LinkedHashMap
        System.out.println(mapper.readValue(json1, Object.class).getClass());
        // array -> ArrayList
        List<Object> array = (List<Object>) mapper.readValue(getJson(Arrays.asList("aa", "bb", "cc")), Object.class);
        System.out.println(array.getClass());
        System.out.println(array.get(0).getClass());
        // bool ->Boolean
        System.out.println(mapper.readValue("true", Object.class).getClass());
    }

    /**
     * jackson序列化对象时，借助对象的getter和setter
     * 如在反序列化时，以getXXX为名的函数会被调用被将其返回序列化
     */
    public static void HighApiCompact() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Student student = new Student();
        student.setName("wang");
        student.setAge(20);

        String json = mapper.writeValueAsString(student);
        System.out.println(json);
    }

    /**
     * 不借助TypeReference进行序列化，会走自动类型映射。
     * 如果类型错误，那么在转型处将出错。
     */
    public static void HighApiParseWithOutTypeReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Student student = new Student();
        student.setName("wang");
        student.setAge(20);

        List<Student> list = Arrays.asList(student);
        String json = mapper.writeValueAsString(list);
        List<Student> data = mapper.readValue(json, List.class); //此时不会遇到问题
        //Student a=data.get(0); //这里存在一个隐式的强制转换，会将Map强制转换为Student导致报错

        //加上泛型参数后，容器的元素会被泛序列成对应的类型
        List<Student> data2 = mapper.readValue(json, new TypeReference<List<Student>>() {
        });
        Student a = data2.get(0);
    }


    /**
     * 使用不整齐的数据序列化json
     */
    public static void ParseBeanByUntidy() throws Exception {
        Student s1 = new Student();
        // 字段被设置为null或基本类型的zero value
        System.out.println(getJson(s1));

        // https://www.baeldung.com/jackson-ignore-null-fields
        // 忽略缺省值
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        System.out.println(mapper.writeValueAsString(s1));

        // 反序列化时可以容忍字段缺失
        String json1 = getJson(Collections.singletonMap("age", 3));
        System.out.println(new ObjectMapper().readValue(json1, Student.class));

        // 反序列化时默认不可以容忍字段多余
        HashMap<String, Object> fieldsValues = new HashMap<>();
        fieldsValues.put("age", 3);
        fieldsValues.put("name", "wang");
        fieldsValues.put("hobby", "fishing");
        String json2 = getJson(fieldsValues);
        // System.out.println(new ObjectMapper().readValue(json2,Student.class));

        // https://stackoverflow.com/questions/5455014/ignoring-new-fields-on-json-objects-using-jackson
        // 忽略多余的字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println(mapper.readValue(json2, Student.class));
    }

    /**
     * 基于底层api解析数据，一般有path或get两种方式
     * 使用path可以处理上层为null的情况，asXXX可以进行基本类型转换
     */
    public static void LowLevel() throws Exception {
        String json1 = "{\"personal\":{\"name\":\"wang\"}}";
        ObjectMapper mapper = new ObjectMapper();
        String name1 = mapper.readTree(json1).get("personal").get("name").textValue();
        String name2 = mapper.readTree(json1).path("personal").path("name").asText();
    }

    public static void main(String[] args) throws Exception {
        LowLevel();
    }

    private static String getJson(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception ignored) {
            return null;
        }
    }
}
