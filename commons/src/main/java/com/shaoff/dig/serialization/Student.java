package com.shaoff.dig.serialization;

import java.io.Serializable;

public class Student extends Person implements Serializable {
    private int no;

    @Override
    public String toString() {
        return "Student{" +
                "no=" + no +
                '}';
    }

    public Student(String id ,int no) {
        super(id);
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

}