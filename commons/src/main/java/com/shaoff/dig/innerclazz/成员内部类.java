package com.shaoff.dig.innerclazz;

/**
 * Author: shaoff
 * Date: 2020/5/15 17:31
 * Package: serialization.org.apache.spark.serialization1.closure1.innerclass1
 * Description:
 */

class Outer1{
    private int a1;
    private static int s1;
    private void f1() {

    }

    class Inner1{
        int a2;
        void f2(){
            //access outer's field,function
            int b=a1; //可以直接引用或是Outer1.this.a1;
            a1=2;
            Outer1.this.f1();
            int c=Outer1.s1;
        }
    }
}
public class 成员内部类 {
    public static void main(String[] args) {
        Outer1 outer=new Outer1();
        Outer1.Inner1 inner=outer.new Inner1();
    }
}
