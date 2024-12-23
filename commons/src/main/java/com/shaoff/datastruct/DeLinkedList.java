package com.shaoff.datastruct;

/**
 * Author: shaoff
 * Date: 2020/12/7 15:22
 * Package: datastruct
 * Description:
 */
public class DeLinkedList<K, V> {
    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K k, V v) {
            this.key = k;
            this.value = v;
        }
    }

    Node<K, V> head, tail;

    void putRight(Node<K, V> node) {
        if (head == null) {
            assert tail == null;
            head = tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = tail.next;
        }
    }

    void putLeft(Node<K, V> node) {
        if (head == null) {
            assert tail == null;
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = head.prev;
        }
    }

    Node<K, V> pollRight() {
        Node<K, V> res;
        if (head == tail) {
            //empty or one
            res = head;
            head = tail = null;
        } else {
            res = tail;
            tail = tail.prev;
            res.prev.next = null;
            res.prev = null;
        }
        return res;
    }

    Node<K, V> pollLeft() {
        Node<K, V> res;
        if (head == tail) {
            //empty or one
            res = head;
            head = tail = null;
        } else {
            res = head;
            head = head.next;
            res.next.prev = null;
            res.next = null;
        }
        return res;
    }

    void insert(int i, Node<K, V> node) {
        if (i == 0 && head == null) {
            assert tail == null;
            head = tail = node;
            return;
        }
        Node<K, V> curr = head;
        for (int j = 0; j < i - 1; j++) {
            if (curr == null) {
                throw new IndexOutOfBoundsException();
            }
            curr = curr.next;
        }
        assert curr != null;
        Node<K, V> next = curr.next;
        curr.next = node;
        node.prev = curr;

        if (next != null) {
            node.next = next;
            next.prev = node;
        } else {
            tail = node;
        }
    }

    void remove(Node<K, V> node) {
        if (node == head) {
            pollLeft();
        } else if (node == tail) {
            pollRight();
        } else {
            if(node.prev==null){
                System.out.println("prev null");
            }
//            System.out.println(node.prev);
            node.prev.next = node.next.next;
            node.next.prev = node.prev;
            node.next = null;
            node.prev = null;
        }
    }

    public static void main(String[] args) {
        DeLinkedList<String, Integer> ls = new DeLinkedList<>();
        ls.putRight(new Node<String, Integer>("wang", 123));
        ls.putRight(new Node<String, Integer>("lee", 123));
        Node<String, Integer> n1 = ls.pollLeft();
        System.out.println(n1.key);
        Node s = new Node("shao", 456);
        ls.insert(1, s);
        System.out.println(s.next);
        System.out.println(s.prev);
//        Node<String, Integer> n2 = ls.pollRight();
        ls.putLeft(new Node("chou", "789"));
        ls.remove(s);

        Node<String, Integer> n3 = ls.pollRight();
        System.out.println(n3.key);

        Node<String, Integer> n4 = ls.pollLeft();
        System.out.println(n4.key);
    }
}