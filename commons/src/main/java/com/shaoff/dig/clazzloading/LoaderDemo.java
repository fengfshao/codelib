package com.shaoff.dig.clazzloading;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Author: shaoff
 * Date: 2020/3/22 03:34
 * Package: classloading1.test
 * Description:
 */
public class LoaderDemo {

    static MyObject loadedByJvm() {
        return new MyObject();
    }

    static void loadedByCode() {
        try {
            // 未执行类的静态初始化块
            Class.forName("classloading1.test.MyObject", false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static Class loadedByAnotherLoader() throws ClassNotFoundException {
        ClassLoader myObjectLoader = new ClassLoader(null) {
            @Override
            public Class<?> findClass(String name) {
                try {
                    InputStream in = new BufferedInputStream(new FileInputStream(
                            "/Users/sakura1/stuff/myprojects/corel/javal/src/main/java/classloading1/test/MyObject.class"));
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
//        Thread.currentThread().setContextClassLoader(myObjectLoader);
        Class res = Class.forName("classloading1.test.MyObject", true, myObjectLoader);
//        System.out.println(res.getClassLoader());
        return res;
    }

    static void testLoadedTwice() throws ClassNotFoundException {
        Class c2 = loadedByAnotherLoader();
        Class c1 = new MyObject().getClass();
        System.out.println(c1.getClassLoader());
        System.out.println(c2.getClassLoader());
        System.out.println(c1.equals(c2));
    }

    public static void main(String[] args) throws Exception {
   //     loadedByJvm();
//        loadedByCode();
  //      loadedByAnotherLoader();
        testLoadedTwice();
    }
}

