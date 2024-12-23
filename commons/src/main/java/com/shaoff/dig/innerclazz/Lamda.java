package com.shaoff.dig.innerclazz;

/**
 * Author: shaoff
 * Date: 2020/5/15 20:01
 * Package: serialization.org.apache.spark.serialization1.closure1.innerclass1
 * Description:
 */
class Outer5 {
    private int a1;
    void f1(){

    }
    void function(){
        int b1=-1;
        Runnable t= () -> {
            int t1 =a1;
            int t2=b1;
            f1();
        };
    }
}
public class Lamda {
    public static void main(String[] args) {
        Outer5 outer5=new Outer5();
        outer5.function();
    }
}
