package com.shaoff.dig.exp;

/**
 * Author: shaoff
 * Date: 2020/12/4 07:16
 * Package: exp
 * Description:
 * 测试异常跨线程抛出
 */
public class ThreadException {
    static volatile Throwable e;
    public static void main(String[] args) throws Exception{
        Runnable target;
        Thread a=new Thread(()->{
            System.out.println("begin");
            throw new RuntimeException("abc");
        });
        a.setDefaultUncaughtExceptionHandler((t,exception)->{
            e=exception;
        });

        a.start();
        a.join();
        System.out.println(e);
    }
}
