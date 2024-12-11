package com.shaoff.dig.serialization.ma1;


import java.io.*;

/**
 * Author: shaoff
 * Date: 2020/5/2 17:18
 * Package: serialization.ma
 * Description:
 */
public class Main {
    public static void main(String[] args) throws Exception{
        testCommon();
    }

    /**
     * 测试对象图中引用相同的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static void testCommon() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.bin"));

        Object a2 =  in.readObject();
        Object b2 =  in.readObject();
//        System.out.println(b2.getCommon()==a2.getCommon());
        System.out.println(a2);
        System.out.println(b2);
    }
}
