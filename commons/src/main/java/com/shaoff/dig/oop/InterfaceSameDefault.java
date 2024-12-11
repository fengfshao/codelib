package com.shaoff.dig.oop;

interface DoA {

    default void do2() {
        System.out.println("do2");
    }

    void do1();
}

interface DoB {

    default void do2() {
        System.out.println("do2");
    }

    void do1();
}

class DoImpl implements DoA, DoB {

    @Override
    public void do2() {
        //当实现的多个接口含有相同的default方式，需要实现类重写该方法
    }

    @Override
    public void do1() {
        System.out.println("do1");
    }
}

public class InterfaceSameDefault {

    public static void main(String[] args) {
        new DoImpl().do1();
    }
}