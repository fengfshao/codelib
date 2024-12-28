package com.shaoff.dig.concurrent;

/**
 * Author: shaoff
 * Date: 2020/5/23 15:09
 * Package: concurrent.jmm
 * Description:
 * 缓存一致性是由多核和高速缓存机制引起的，在硬件层面能够保证缓存一致性'cache coherency'，
 * 可能由缓存一致性协议，（窃听和基于目录），如MESI,MOESI,MESIF等或是其他方案解决
 * 那么为什么需要volatile，java中所谓的volatile中的，变量在多线程中的可见性指什么
 * https://www.zhihu.com/question/296949412
 * https://stackoverflow.com/questions/43492181/does-volatile-keword-in-java-really-have-to-do-with-caches
 */

public class Volatile1 {
    public  int i = 0;
    int a=0;
    public void increment(){
        i++;
        a=10;
    }

    public static void main(String[] args) {
        Volatile1 v=new Volatile1();
        for(int i=0;i<1;i++){
            v.increment();
        }
    }

}

