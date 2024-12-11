package com.shaoff.dig.oop;

/**
 * Author: shaoff
 * Date: 2020/9/14 11:17
 * Package: base
 * Description:
 * 1.函数重载时具体调用的函数由引用类型决定，即静态绑定
 * 2.与返回类型无关
 * 3.与异常声明无关
 */
public class OverLoadDemo {
    void methodA(Object a){
        System.out.println("methodA for object");
    }

    /* 返回类型无关
    int methodA(Object a){
        System.out.println("methodA for object");
        return 1;
    }*/

    /*和异常声明无关
    void methodA(String a) throw Exception{
        System.out.println("methodA for string");
    }*/


    void methodA(String a){
        System.out.println("methodA for string");
    }

    public static void main(String[] args) {
        OverLoadDemo  d=new OverLoadDemo();
        Object a="abc";
        d.methodA(a);

    }

}
