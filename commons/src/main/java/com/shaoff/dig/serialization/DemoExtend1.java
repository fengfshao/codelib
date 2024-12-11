package com.shaoff.dig.serialization;

import java.io.*;

/**
 * Author: shaoff
 * Date: 2020/5/19 17:44
 * Package: serialization.extend1
 * Description:
 */

class Person{
    private String id;

    public Person(String id) {
        System.out.println("youcacn");
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

public class DemoExtend1{
    public static void main(String[] args) {
        try {
            //序列化
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("2.txt"));
//            Person person = new Student("111",1);
//            objectOutputStream.writeObject(new Object());
            //反序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("2.txt"));
            Person person1 = (Person) objectInputStream.readObject();
            System.out.println(person1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
