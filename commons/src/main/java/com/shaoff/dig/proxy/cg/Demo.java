package com.shaoff.dig.proxy.cg;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;

/**
 * Author: shaoff
 * Date: 2020/5/9 11:04
 * Package: proxy.动态.CG
 * Description:
 */

public class Demo {
    public static void main(String[] args) {
//       /* System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/sakura/tmp/cglib");
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(Dog.class);
        //设置回调函数
        enhancer.setCallback(new TimerMethodInterceptor());
        Dog proxyDog = (Dog) enhancer.create();
        //调用代理类的eat方法

        proxyDog.eat();
        System.out.println(proxyDog.getClass().getSimpleName());
        Method[] ms = proxyDog.getClass().getMethods();
        for (Method m : ms) {
            System.out.println(m.getName());
        }
    }
}
