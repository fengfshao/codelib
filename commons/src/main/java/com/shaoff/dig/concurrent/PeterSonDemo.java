package com.shaoff.dig.concurrent;

import java.util.concurrent.*;

/**
 * 1.互斥
 * 2.有限等待
 * 3.前进
 */
public class PeterSonDemo {
    static volatile boolean[] flag=new boolean[2];
    static volatile byte turn=0;

    static volatile int total=0;

    public static void main(String[] args) throws InterruptedException {
       Thread th1=new Thread(()->{
            for(int i=0;i<100;i++){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag[0]=true;
                turn=1;
                while(flag[1]&&turn==1);
                total+=1;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag[0]=false;
            }
        });

       Thread th2=new Thread(()->{
            for(int i=0;i<100;i++){
                flag[1]=true;
                turn=0;
                while(flag[0]&&turn==0);
                total-=1;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag[1]=false;
            }
        });
        th1.start();th2.start();
        th1.join();th2.join();

        System.out.println(total);
    }
}
