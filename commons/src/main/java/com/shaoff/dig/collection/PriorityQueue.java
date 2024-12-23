package com.shaoff.dig.collection;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 对堆的操作通常是在第一个位置（顶）或最后一个位置（底），要进行调整时分别是向下或向上调整
 * 在原地建堆时，从后一个非叶结点开始，对每个结点处理时，其都处于堆顶的位置，因此需要进行siftDown操作
 */
public class PriorityQueue<T> extends AbstractQueue<T> {
    ArrayList<T> data = new ArrayList<>();

    public PriorityQueue() {
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return data.size();
    }

    @Override
    public boolean offer(T t) {
        data.add(t);
        siftUp(data.size() - 1);
        return true;
    }

    @Override
    public T poll() {
        T res = data.get(0);
        T value = data.remove(data.size() - 1);
        if (data.size() > 0) {
            data.set(0, value);
            siftDown(0);
        }
        return res;
    }

    @Override
    public T peek() {
        return data.get(0);
    }

    public PriorityQueue(List<T> data) {
        this.data.addAll(data);
        for (int i = parent(size() - 1); i >= 0; i--)
            siftDown(i);
    }

    private int left(int p) {
        //return p << 1 + 1;
        return 2 * p + 1;
    }

    private int parent(int c) {
        if (c == 0) return -1;
        return (c - 1) / 2;
    }

    // 对当前堆进行上浮操作，i表示新加入的元素的位置
    @SuppressWarnings("unchecked")
    private void siftUp(int i) {
        int cur = i;
        int parent = parent(cur);
        Comparable<? super T> value = (Comparable<? super T>) data.get(cur);
        // min-heap
        while (cur > 0 && value.compareTo(data.get(parent)) < 0) {
            data.set(cur, data.get(parent)); //pull parent value down
            cur = parent;
            parent = parent(cur);
        }
        data.set(cur, (T) value);
    }

    // i后的元素组成堆，i表示调整堆使得i处的元素也符合堆
    @SuppressWarnings("unchecked")
    private void siftDown(int i) {
        int end = size();
        int cur = i;
        int child = left(cur);
        Comparable<? super T> keyVal = (Comparable<? super T>) data.get(cur);
        while (child < end) {
            Comparable<? super T> tmpValue = (Comparable<? super T>) data.get(child);
            if (child + 1 < end && tmpValue.compareTo(data.get(child + 1)) > 0) {
                child = child + 1;
            }
            // min-heap
            if (keyVal.compareTo(data.get(child)) < 0)
                break;
            data.set(cur, data.get(child));
            cur = child;
            child = left(cur);
        }
        data.set(cur, (T) keyVal);
    }

    public static void main(String[] args) {
        int[] arr = new int[]{4, 3, 7, 6, 2, 1, 5};
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for (int num : arr) {
            q.offer(num);
        }
        System.out.println(q.data);
        while (!q.isEmpty()) {
            System.out.println(q.poll());
        }
        System.out.println(q.data.isEmpty());
    }
}
