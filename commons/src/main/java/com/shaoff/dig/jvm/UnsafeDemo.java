package com.shaoff.dig.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeDemo {

    private static int apple = 10;
    private int orange = 10;

    public static void main(String[] args) throws Exception {
        Unsafe unsafe = getUnsafeInstance();
        Field appleField = UnsafeDemo.class.getDeclaredField("apple");
        long appleOffset=unsafe.staticFieldOffset(appleField);
        System.out.println("Location of Apple: " + appleOffset);

        Field orangeField = UnsafeDemo.class.getDeclaredField("orange");
        long orangeOffset= unsafe.objectFieldOffset(orangeField);
        System.out.println("Location of Orange: " + orangeOffset);

        unsafe.putInt(UnsafeDemo.class,appleOffset,10000);
        System.out.println(apple);
    }

    private static Unsafe getUnsafeInstance() throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        return (Unsafe) theUnsafeInstance.get(UnsafeDemo.class);
    }
}
