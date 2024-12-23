package com.shaoff.dig.collection;


import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 基于循环队列和动态数组的实现
 */
public class ArrayDequeue<T> implements Deque<T> {

    private Object[] elements = new Object[16];
    private int head;
    private int tail; // 下一个元素的插入位置

    @Override
    public void addFirst(T t) {
        head = head - 1 & (elements.length - 1);
        elements[head] = t;
        // 与教材中的固定大小的循环数组略为不同
        if (head == tail) {
            doubleCapacity();
        }
    }

    @Override
    public void addLast(T t) {
        tail = tail + 1 & (elements.length - 1);
        elements[tail] = t;
        if (tail == head) {
            doubleCapacity();
        }
    }

    @Override
    public boolean offerFirst(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public boolean offerLast(T t) {
        addLast(t);
        return true;
    }

    @Override
    public T removeFirst() {
        @SuppressWarnings("unchecked")
        T res = (T) elements[head];
        elements[head] = null;
        head = head + 1 & (elements.length - 1);
        return res;
    }

    @Override
    public T removeLast() {
        @SuppressWarnings("unchecked")
        T res = (T) elements[tail];
        elements[tail] = null;
        tail = tail - 1 & (elements.length - 1);
        return res;
    }

    @Override
    public T pollFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeFirst();
    }

    @Override
    public T pollLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeLast();
    }

    @Override
    public T getFirst() {
        return (T) elements[head];
    }

    @Override
    public T getLast() {
        return (T) elements[(tail - 1) & (elements.length - 1)];
    }

    @Override
    public T peekFirst() {
        return getFirst();
    }

    @Override
    public T peekLast() {
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public boolean offer(T t) {
        return offerFirst(t);
    }

    @Override
    public T remove() {
        return removeFirst();
    }

    @Override
    public T poll() {
        return pollFirst();
    }

    @Override
    public T element() {
        return getFirst();
    }

    @Override
    public T peek() {
        return getFirst();
    }

    @Override
    public void push(T t) {
        offerFirst(t);
    }

    @Override
    public T pop() {
        return pollFirst();
    }

    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException("Sorry, deque too big");
        Object[] a = new Object[newCapacity];
        int r = n - p; // number of elements to the right of p
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        elements = new Object[16];
        head = tail = 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1); // (tail-head+element.length)%(elements.length-1)
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Deque<String> dq = new ArrayDequeue<>();
        dq.add("1");
        dq.add("2");
        dq.add("3");
        System.out.println(dq.remove());
        dq.push("0");
        System.out.println(dq.peek());
    }
}
