package com.shaoff.dig.basic.collection;

import java.util.HashMap;

/**
 * Author: shaoff
 * Date: 2020/9/15 20:09
 * Package: collection
 * Description:
 */
public class HashMapDemo {
    static HashMap<String,Integer> map=new HashMap<>();

    static void constructor(){
        new HashMap<>();
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static final int tableSizeFor(int cap) {
        final int MAXIMUM_CAPACITY = 1 << 30;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        //101
        //100
        //
        int size=tableSizeFor(5);
        System.out.println(size);
    }
}
