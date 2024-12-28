package com.shaoff.dig.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: shaoff
 * Date: 2020/2/17 22:04
 * Package: object
 * Description:
 */

@Data
@AllArgsConstructor
class Student implements Cloneable {
    private String name;
    private int age;

    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }
}

@Data
@AllArgsConstructor
class Teacher {
    private String name;
    private int age;

    @Override
    public Teacher clone() throws CloneNotSupportedException {
        return (Teacher) super.clone();
    }
}

public class CloneDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        Student s1=new Student("wang",17);
        Teacher t1=new Teacher("lee",17);
        Student s2=s1.clone();
        Teacher t2=t1.clone();
    }
}
