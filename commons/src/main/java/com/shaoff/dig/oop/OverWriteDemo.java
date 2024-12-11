package com.shaoff.dig.oop;

import java.io.IOException;

/**
 * Author: shaoff
 * Date: 2020/9/14 11:24
 * Package: base
 * Description:
 * 1.重写动态绑定
 * 2.支持返回类型协变，不支持入参逆变
 * 3.异常声明不能增加或泛化，可以具体化
 */
public class OverWriteDemo {
    private static class Base{
        void testA(Number c){

        }
        void testC(Integer c) throws Exception {

        }
        Object testB(){
            return null;
        }
    }
    private static class Child extends Base{

        /* 无法进行参数逆变
        void testA(Object c) {
        }*/

        /*不能增加或泛化异常类型
        void testA(Integer c) throws Exception{

        }*/

        /*可以具体化异常类型*/
        @Override
        void testC(Integer c) throws IOException{

        }
        @Override
        String testB(){
            return "";
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
