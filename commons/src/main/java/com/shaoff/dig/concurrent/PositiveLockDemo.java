package com.shaoff.dig.concurrent;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: shaoff
 * Date: 2020/6/2 18:54
 * Package: concurrent.test
 * Description:
 */
public class PositiveLockDemo {
    static int sum=0;
    static AtomicInteger sum1=new AtomicInteger(0);

    static ThreadPoolExecutor executor= new ThreadPoolExecutor(5, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    static void negative(){
        for(int i=0;i<5;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        synchronized (PositiveLockDemo.class){
                            sum+=1;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
        boolean terminated=false;
        while (!terminated){
            try {
                terminated=executor.awaitTermination(3,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //
            }
        }
        System.out.println("thcount:"+executor.getActiveCount());
        System.out.println("sum:"+sum);
    }

    static void positive(){
        for(int i=0;i<5;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        int cur;
                        do{
                            cur=sum1.get();
                            System.out.println(cur);
                        }while (!sum1.compareAndSet(cur,cur+1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
        boolean terminated=false;
        while (!terminated){
            try {
                terminated=executor.awaitTermination(3,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //
            }
        }
        System.out.println("thcount:"+executor.getActiveCount());
        System.out.println("sum:"+sum1.get());
    }

    static Random rand=new Random();
    /*每次只有一个线程成功，这样想得到sum为5，需要每个线程尝试五次*/
    static void positive2(){
        for(int i=0;i<5;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(rand.nextInt(3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<5;i++){
                        int cur=sum1.get();
                        if(cur<5&&sum1.compareAndSet(cur,cur+1)){
                            System.out.println(Thread.currentThread().getName()+"added one");
                        }
                        try {
                            Thread.sleep(rand.nextInt(1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        executor.shutdown();
        boolean terminated=false;
        while (!terminated){
            try {
                terminated=executor.awaitTermination(3,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //
            }
        }
        System.out.println("thcount:"+executor.getActiveCount());
        System.out.println("sum:"+sum1.get());
    }

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        positive2();
        System.out.println(System.currentTimeMillis()-start+" ms");
    }
}
