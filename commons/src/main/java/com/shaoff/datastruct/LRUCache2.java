package com.shaoff.datastruct;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: shaoff
 * Date: 2020/9/15 19:16
 * Package: datastruct
 * Description:
 * <p>
 * 使用LinkedHashMap实现一个
 */
public class LRUCache2<K, V> {

    private static class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V>{
        //定义缓存的容量
        private int capacity;
        private static final long serialVersionUID = 1L;
        //带参数的构造器
        LRULinkedHashMap(int capacity){
            //调用LinkedHashMap的构造器，传入以下参数
            super(16,0.75f,true);
            //传入指定的缓存最大容量
            this.capacity=capacity;
        }
        //实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
        @Override
        public boolean removeEldestEntry(Map.Entry<K, V> eldest){
            return size()>capacity;
        }
    }

    private LinkedHashMap<K, V> data;

    public LRUCache2(int capacity) {
        data = new LRULinkedHashMap<K,V>(capacity);
    }

    public void put(K key, V value) {
        data.put(key,value);
    }

    public V get(K key) {
        return data.get(key);
    }

}
