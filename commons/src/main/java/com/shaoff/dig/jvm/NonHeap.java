package com.shaoff.dig.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Author: shaoff
 * Date: 2020/5/13 18:16
 * Package: jvm.memory
 * Description:
 */
public class NonHeap {
    static void nonHeap1() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe us = (Unsafe) f.get(null);
        Long addr = us.allocateMemory(1024 * 1024);
//        us.freeMemory(1024);
    }

    static void nonHeap2() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
    }

    public static void main(String[] args) throws Exception {
//        Unsafe unsafe = Unsafe.getUnsafe();
//        Field f = Unsafe.class.getDeclaredField("theUnsafe");
//        f.setAccessible(true);
//        Unsafe us = (Unsafe) f.get(null);
//        Long addr=us.allocateDirect(1024);
//        us.reallocateMemory(1024, 1024);
//        us.freeMemory(1024);
//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*10);
        nonHeap2();
        Thread.sleep(1000 * 60 * 10);
    }


}
