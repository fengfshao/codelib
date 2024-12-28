package com.shaoff.dig.proxy.handler;

import java.lang.reflect.Proxy;
import java.util.function.Function;

import java.lang.reflect.Method;

/**
 * Author: shaoff
 * Date: 2020/6/23 15:11
 * Package: proxy.handlers.Demo
 * Description:
 */

public class InvocationHandlerDemo {

    static void testFunction(){
        Function<String,Void> sayHi=new Say<>();
        sayHi.apply("hi");
    }

    static void testProxy(){
        Say<String> sayHello= new Say<>();
        TimerHandler t=new TimerHandler(sayHello);
        Function<String,Void> proxy= wrap(t,Function.class);
        long start= System.currentTimeMillis();
        proxy.apply("hello");
        System.out.println("duration: "+(System.currentTimeMillis()-start));


        System.out.println(proxy.getClass().getSimpleName());
        for(Method m:proxy.getClass().getMethods()){
            System.out.println(m.getName());
        }
    }

    /**
     * java -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
     */
    public static void main(String[] args) throws Exception {
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        testProxy();
    }

    public static <T> T wrap(java.lang.reflect.InvocationHandler h,Class<T> interface0){
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{interface0} , h);
    }

    public static Object wrap(java.lang.reflect.InvocationHandler h,Class<?>[] interfaces){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),interfaces, h);
    }
}
