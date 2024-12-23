package com.shaoff.datastruct;

import java.util.Stack;

public class Test {
    public static void main(String[] args) {
        Stack<Integer> arr=new Stack<>();
        CycleQueue<Integer> queue=new CycleQueue<>(4);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());

        System.out.println(queue.size());
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue.size());
    }
}
