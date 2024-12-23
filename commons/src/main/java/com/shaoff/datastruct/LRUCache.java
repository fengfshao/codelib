package com.shaoff.datastruct;


import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;
        Node<K, V> prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    Node<K, V> head, tail;

    private void offerLeft(Node<K, V> node) {
        if (head == null) {
            assert tail == null;
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    private void offerRight(Node<K, V> node) {
        if (head == null) {
            assert tail == null;
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private Node<K, V> pollLeft() {
        if (head == null) {
            return null;
        } else if (head == tail) {
            Node<K, V> res = head;
            head = tail = null;
            return res;
        } else {
            Node<K, V> res = head;
            head = head.next;
            res.next = null;
            head.prev = null;
            return res;
        }
    }

    private Node<K, V> pollRight() {
        if (head == null) {
            return null;
        } else if (head == tail) {
            Node<K, V> res = head;
            head = tail = null;
            return res;
        } else {
            Node<K, V> res = tail;
            tail = tail.prev;
            res.prev = null;
            tail.next = null;
            return res;
        }
    }

    private void remove(Node<K, V> node) {
        if (head == node) {
            pollLeft();
        } else if (tail == node) {
            pollRight();
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
    }

    Map<K, Node<K, V>> map = new HashMap<>();
    int capacity;
    int size;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    void put(K key, V value) {
        Node<K, V> target = map.get(key);
        if (target != null) {
            target.value = value;
            remove(target);
            offerRight(target);
        } else {
            target = new Node<>(key, value);
            map.put(key, target);
            offerRight(target);
            if (++size > capacity) {
                Node<K, V> r = pollLeft();
                assert r != null;
                map.remove(r.key);
            }
        }

    }

    V get(K key) {
        Node<K, V> target = map.get(key);
        if (target != null) {
            remove(target);
            offerRight(target);
        }
        return target == null ? null : target.value;
    }


    public static void main(String[] args) {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("shao", 123);
        cache.put("wang", 456);
        cache.put("lee", 789);
        cache.put("shao", 444);
        cache.put("zee", 000);
        System.out.println(cache.get("shao"));

        System.out.println(cache.get("wang"));


    }
}
