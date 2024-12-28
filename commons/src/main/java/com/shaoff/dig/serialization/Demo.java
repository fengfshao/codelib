package com.shaoff.dig.serialization;

import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Demo {

    public static void basicSerialize() throws Exception {
        //        ObjectOutputStream out = new ObjectOutputStream(
//                Files.newOutputStream(Paths.get("students.dat")));
//        Student st=new Student("shao",20,90);
//        out.writeObject(st);
        ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(Paths.get("students.dat")));
        // static block会被初始化，构造函数和动态块不会执行
        Student newInstance = (Student) ois.readObject();
        System.out.println(newInstance);
    }

    public static void main(String[] args) throws Exception {
        basicSerialize();
    }
}
