package me.fengfshao.loadonline;

import com.google.common.primitives.UnsignedLong;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import me.fengfshao.common.annotations.Udf;
import me.fengfshao.sqlparse.udf.ScalarFunction;
import org.reflections.Reflections;

/**
 * Author: fengfshao
 * Date: 2021/12/9 11:35
 * Package: me.fengfshao.loadonline
 * Description:
 */
public class LoadJar {

    /*public static void main(String[] args) throws MalformedURLException {
        // 从本地classpath扫描
        UdfFactory.cache.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });

        String jar1 = "file:///Users/sakura1/tmp/oneshot-1.0-SNAPSHOT.jar";
        String jar2 = "/Users/sakura1/tmp/oneshot-1.1-SNAPSHOT.jar";
        //
        URL[] urls = new URL[1];
        urls[0] = new URL(jar1);
        URLClassLoader urlClassLoader = new URLClassLoader(urls);

        System.out.println("=================");
        Reflections reflections = new Reflections("me.fengfshao.trs", urlClassLoader);
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
        System.out.println(i);
    }*/

}
