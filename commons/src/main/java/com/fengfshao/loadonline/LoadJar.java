package com.fengfshao.loadonline;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * Author: fengfshao
 * Date: 2021/12/9 11:35
 * Package: me.fengfshao.loadonline
 * Description:
 */
public class LoadJar {

    static Exception lastEx;
    public static void main(String[] args) throws MalformedURLException {
        for(int i=0;i<10;i++){
            try {
                f1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*List<String> li = Arrays.asList("8.0.0", "10.1.0");
        System.out.println(li);
        // 从本地classpath扫描
        List<String> sre = Arrays.asList("aaa", "sf", "cc");
        sre.forEach(str -> {
            switch (str) {
                case "aaa":
                    System.out.println("a");
                    break;
                case "cc":
                    System.out.println("c");
                    break;
                case "sf":
                    System.out.println("s");
                    break;
                default:
                    throw new UnsupportedOperationException("");
            }
        });*/
/*
        UdfFactory.cache.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
*/

        String jar1 = "file:///Users/sakura1/tmp/oneshot-1.0-SNAPSHOT.jar";
        String jar2 = "/Users/sakura1/tmp/oneshot-1.1-SNAPSHOT.jar";
        //
        URL[] urls = new URL[1];
        urls[0] = new URL(jar1);
        URLClassLoader urlClassLoader = new URLClassLoader(urls);

        System.out.println("=================");
        //Runtime.getRuntime().
        /*Reflections reflections = new Reflections("com.xxx.trs.itemflow.extension", urlClassLoader);
        Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Udf.class);
        clazzs.forEach((c) -> {
            try {
                ScalarFunction f = (ScalarFunction) c.newInstance();
                System.out.println(f.getName() + ":" + f.getClass().getName());
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        });
        System.out.println("=================");
        UdfFactory.cache.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });

        long l = 13234L;

        Integer i = Integer.valueOf((int) l);
        UnsignedLong ul = UnsignedLong.valueOf(2147483648L);

        System.out.println(UnsignedLong.fromLongBits(-1L));
        int a = Long.valueOf(24387837).intValue();
        System.out.println(i);*/
    }

    static void   f1() throws Exception {
        if(lastEx!=null){
            throw new IllegalAccessException("/..");
        }
        int n=new Random().nextInt(3);
        if(n==0){
            lastEx = new RuntimeException("aa");
        }
        System.out.println("...");
    }

}
