package com.shaoff.dig.serialization;

import java.io.*;

class TestStudent implements Serializable {
    private static final long serialVersionUID=1L;
    String name;
    int age;
    double score;

    public TestStudent() {
        System.out.println("default constructor invoked");
    }

    private TestStudent(String name) {
        this.name = name;
    }

    public TestStudent(String name, int age, double score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }
    /*private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        System.out.println("ll");
    }*/

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }

    static {
        System.out.println("static block once");
    }
    {
        System.out.println("dynamic block once");
    }

    /*private Object readResolve() {
        return new Student("wang");
    }*/
}

public class Test{
    public static void main(String[] args) throws Exception {

//        Student[] students=new Student[1];
//        students[0]=new Student("shao",20,90);
        /*ObjectOutputStream out = new ObjectOutputStream(
                (new FileOutputStream("/Users/sakura/stuff/stuff-projects/corel/javal/students.dat")));
        Student st=new Student("shao",20,90);
        out.writeObject(st);*/

//        Student st=new Student("shao",20,90);
//        Class studentClass=Class.forName("serialization.Student");
//        Student st= (Student) studentClass.getConstructor().newInstance();
        testReflect();
    }

    //反序列化不会执行构造函数，动态初始化块等
    static void testDeserialize() throws Exception{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/sakura/stuff/stuff-projects/corel/javal/students.dat"));
        TestStudent newInstance = (TestStudent) ois.readObject();
        System.out.println(newInstance);
    }

    //反射需要无参构造函数，由于动态初始化块在字节码层面被插入到所有构造函数字节码的前面，也会执行
    static void testReflect() throws Exception{
        Class<TestStudent> clazz= (Class<TestStudent>) Class.forName("serialization.Student");
         TestStudent st=clazz.newInstance();
        System.out.println(st);
    }
}