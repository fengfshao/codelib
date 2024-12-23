package com.shaoff.dig.innerclazz;

/**
 * Author: shaoff
 * Date: 2020/5/15 19:30
 * Package: serialization.org.apache.spark.serialization1.closure1.innerclass1
 * Description:
 */
class Outer4{
    int a1;
    void function(){
        int used=0;
        int notUsed=-1;
        Runnable t=new Runnable() {
            @Override
            public void run() {
//                int t1=used;
//                int t2=a1;
            }
        };
    }
}
public class 匿名内部类 {
    public static void main(String[] args) {
        Outer4 outer4=new Outer4();
        outer4.function();
    }
}
