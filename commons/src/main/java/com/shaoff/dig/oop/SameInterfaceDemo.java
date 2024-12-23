package com.shaoff.dig.oop;

/**
 * Author: shaoff
 * Date: 2020/4/23 20:33
 * Package: object.interface1
 * Description:
 */

interface Hello1{
    void sayHello();
}
interface Hello2{
    void sayHello();
}

class Student implements Hello1,Hello2{
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}

public class SameInterfaceDemo {
    /*测试实现的接口中有相同的方法*/
    static void testSameInterfaceMethod(){
        Student st=new Student();
        st.sayHello();
    }
    public static void main(String[] args) {

    }
}

