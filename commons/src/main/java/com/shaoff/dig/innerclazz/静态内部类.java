package com.shaoff.dig.innerclazz;

/**
 * Author: shaoff
 * Date: 2020/5/15 17:50
 * Package: serialization.org.apache.spark.serialization1.closure1.innerclass1
 * Description:
 */
class Outer2{
    private int a1;
    void f1(){

    }
    static class Inner2{
        int a2;
        void f2(){
            //can't access outer's non-static
//            int b=Outer2.this.a1;
        }
    }
}
public class 静态内部类 {
    public static void main(String[] args) {
        Outer2 outer2=new Outer2();
        Outer2.Inner2 inner2=new Outer2.Inner2();
    }
}
