package me.fengfshao.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author: fengfshao
 * Date: 2021/6/25 15:19
 * Package: me.fengfshao.common.json
 * Description:
 *
 * 使用JackSon进行json序列化和反序列化
 */
public class JackSon {

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
    }

    public static void HighApiParse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"abc\": \"def\",\"xyz\": 123}";
        //这里传入TypeReference的子类，借助java的泛型机制是泛型类中的泛型不会被擦除（还有成员变量的参数等）
        //的特性，达到实现传递具体泛型参数的目的。
        Map<String, Object> data = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(data);
    }

    /**
     * jackson序列化对象时，借助对象的getter和setter
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
     * 假设不借助TypeReference会如何
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
        List<Student> data2 = mapper.readValue(json, new TypeReference<List<Student>>(){
        });
        Student a=data2.get(0);
    }

    public static void main(String[] args) throws Exception {
        HighApiParseWithOutTypeReference();
    }
}
