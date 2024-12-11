package com.shaoff.dig.serialization.transitive1;

import java.io.*;

/**
 * Author: shaoff
 * Date: 2020/5/15 15:44
 * Package: serialization.transitive1
 * Description:
 */
class Room{
    Person p1=new Person("wang",19);
}
class Bag {
    void put(){

    }
}
class Person implements Serializable{
    String name;
    int age;
    Bag b=new Bag();
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    void buy(){
        b.put();
    }
}
public class Demo {
    static ObjectInputStream ois;
    static ObjectOutputStream out ;

    static {
        try {
//            ois = new ObjectInputStream(new FileInputStream("/Users/sakura/stuff/stuff-projects/corel/javal/src/main/resources/objects.dat"));
            out=new ObjectOutputStream((new FileOutputStream("/Users/sakura/stuff/stuff-projects/corel/javal/src/main/resources/objects.dat")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Room r1=new Room();
        out.writeObject(r1.p1);
    }
}

