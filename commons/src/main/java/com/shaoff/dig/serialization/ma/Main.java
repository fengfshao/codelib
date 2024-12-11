package com.shaoff.dig.serialization.ma;

import java.io.*;

/**
 * Author: shaoff
 * Date: 2020/5/2 17:18
 * Package: serialization.ma
 * Description:
 */
public class Main {
    static int a=101;
    static byte b= (byte) 0xab;
    public static void main(String[] args) throws Exception{
        byte c= (byte) 0xac;
        testCommon();
    }

    /**
     * 测试对象图中引用相同的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static void testCommon() throws IOException, ClassNotFoundException {
        Common c = new Common("common");
        A a = new A("a", c);
        B b = new B("b", c);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        FileOutputStream fout= new FileOutputStream("data.bin");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(a);
        out.writeObject(b);
        out.close();

        FileInputStream fin= new FileInputStream("data.bin");
        /*ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bout.toByteArray()));
        */
        ObjectInputStream in = new ObjectInputStream(fin);
        A a2 = (A) in.readObject();
        B b2 = (B) in.readObject();
        System.out.println(b2.getCommon()==a2.getCommon());
    }
}
