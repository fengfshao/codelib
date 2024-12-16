package com.shaoff.dig.basic.collection.my;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 基于AVL树实现
 */
public class MyTreeMap<K, V> implements Map<K, V> {
    Entry<K, V> root;
    private int size;

    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        /**
         * 记录高度值计算平衡因子
         */
        int h;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.h = 1;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> en = find(root, key);
        if (en != null) {
            return en.value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        return add(key, value);
    }

    @Override
    public V remove(Object key) {
        return null;
    }


    @Override
    public void clear() {

    }

    private V add(K key, V val) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> pre = null;
        Entry<K, V> cur = root;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            pre = cur;
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                V oldValue = cur.value;
                cur.setValue(val);
                return oldValue;
            }
        }
        Entry<K, V> newNode = new Entry<>(key, val);
        size += 1;
        if (pre == null) {
            root = newNode;
        } else {
            int cmp = k.compareTo(pre.key);
            newNode.parent = pre;
            if (cmp < 0) {
                pre.left = newNode;
            } else {
                pre.right = newNode;
            }
        }
        // 自底向上更新每个父结点的高度
        while (pre != null) {
            pre.h = 1 + Math.max(getH(pre.left), getH(pre.right));
            pre = pre.parent;
        }
        return null;
    }


    private Entry<K, V> find(Entry<K, V> root, Object val) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) val;
        Entry<K, V> cur = root;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    private static int getH(Entry en) {
        if (en == null)
            return 0;
        else return en.h;
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Map<Integer, String> testMap = new MyTreeMap<>();
        MyTreeMap<Integer, String> debugMap = (MyTreeMap<Integer, String>) testMap;
        testMap.put(4, "a");
        testMap.put(5, "c");
        testMap.put(6, "b");
        assert testMap.containsKey(3);
        System.out.println(debugMap.root);
    }
}
