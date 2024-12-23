package com.shaoff.dig.oop;

import java.io.IOException;

class Base {

    void doSome() throws Exception {

    }

    void doSome2() throws Exception{
    }

    void doSafe() {

    }

    public Base() throws IOException {
    }
}

/*
1.父类方法带有异常受检的异常声明，子类重写时如果调用了父类的方法，也需要添加异常声明
2.父类方法没有受检的异常声明，子类重写时也不能添加受检的异常声明
*/
class Spec1 extends Base {
    @Override
    void doSome()  {
        System.out.println("do spec1");
    }

    @Override
    void doSome2() throws Exception {
        super.doSome2();
        System.out.println("do spec2");
    }

//    @Override
//    void doSafe() throws IOException{
//        System.out.println("...");
//    }

    /**
     * 因为默认构造函数隐式地调用了super,并且父类的构造函数有异常声明，此时子类的构造函数中也应当包含这些抛出声明
     * 抛出声明的异常类应当向上扩展
     *
     * @throws IOException
     */
    public Spec1() throws Exception {
    }
}

public class 重载 {
    public static void main(String[] args) throws Exception {
        Base b1 = new Spec1();
        b1.doSome2();
    }
}
