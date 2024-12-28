package com.shaoff.dig.serialization;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Author: shaoff
 * Date: 2020/5/15 15:44
 * Package: serialization.transitive1
 * Description:
 */
class Room implements Serializable {
    Person p1 = new Person("wang", 19);
}

class Bag implements Serializable{
    void put() {}
}

class Person implements Serializable {

    String name;
    int age;
    Bag b ;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void buy() {
        b.put();
    }
}

public class TransitiveDemo {

    public static void main(String[] args) throws Exception {
//        ObjectOutputStream roomOut = new ObjectOutputStream((Files.newOutputStream(
//                Paths.get("room.dat"))));
//        Room r1 = new Room();
//        Person p2 = new Person("jx", 32);
//        Bag bag=new Bag();
//        p2.b = r1.p1.b=bag;
//        roomOut.writeObject(r1);
//        roomOut.writeObject(p2);
//        roomOut.close();
//        System.out.println(r1.p1.b==p2.b);

        /* 引用相同的对象反序列化后解析出来也是同一个，但是需要是同一个ObjectInputStream */
        ObjectInputStream roomInput = new ObjectInputStream(Files.newInputStream(Paths.get("room.dat")));
        Room r1= (Room) roomInput.readObject();
        Person p2= (Person) roomInput.readObject();
        System.out.println(r1.p1.b==p2.b);
    }
}

