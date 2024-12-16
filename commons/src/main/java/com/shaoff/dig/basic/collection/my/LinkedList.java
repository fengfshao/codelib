package com.shaoff.dig.basic.collection.my;

import java.util.*;

public class LinkedList<T> extends AbstractSequentialList<T>
        implements List<T>, Deque<T> {

    private transient int size;
    private transient int modCount;
    private transient Node<T> first;
    private transient Node<T> last;

    private static class Node<T> {
        public Node(Node<T> p, T ele, Node<T> n) {
            data = ele;
            prev = p;
            next = n;
        }

        private T data;
        private Node<T> prev;
        private Node<T> next;
    }

    public LinkedList() {
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(T t) {
        linkLast(t);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public boolean remove(Object o) {
        for (Node<T> x = first; x != null; x = x.next) {
            if (Objects.equals(o, x.data)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(T t) {
        return add(t);
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
        return peekFirst();
    }

    @Override
    public void push(T t) {
        addFirst(t);
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<T> node = first; node != null; node = node.next) {
            if (Objects.equals(o, node.data)) {
                return index;
            }
            index += 1;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        int res = index;
        for (Node<T> node = first; node != null; node = node.next) {
            if (Objects.equals(o, node.data)) {
                res = index;
            }
            index += 1;
        }
        return res;
    }

    private void linkFirst(T ele) {
        Node<T> newNode = new Node<>(null, ele, first);
        if (first == null) {
            first = last = newNode;
        } else {
            first.prev = newNode;
            first = newNode;
        }
        size += 1;
        modCount += 1;
    }

    private void linkLast(T ele) {
        Node<T> newNode = new Node<>(last, ele, null);
        if (last == null) {
            last = first = newNode;
        } else {
            last.next = newNode;
            last = last.next;
        }
        size += 1;
        modCount += 1;
    }

    private T unlink(Node<T> x) {
        final T ele = x.data;
        if (x.prev == null) {
            first = x.next;
            first.prev = null;
        } else if (x.next == null) {
            last = x.prev;
            last.next = null;
        } else {
            x.prev.next = x.next;
            x.next.prev = x.prev;
            // help gc
            x.prev = null;
            x.next = null;
        }

        // help gc
        x.data = null;
        size--;
        modCount++;
        return ele;
    }

    public void clear() {
        first = last = null;
        size = 0;
        modCount++;
    }

    @Override
    public void addFirst(T t) {
        linkFirst(t);
    }

    @Override
    public void addLast(T t) {
        linkLast(t);
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
        if (first == null) {
            throw new NoSuchElementException();
        }
        return unlink(first);
    }

    @Override
    public T removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return unlink(last);
    }

    @Override
    public T pollFirst() {
        if (first == null) {
            return null;
        } else {
            return unlink(first);
        }
    }

    @Override
    public T pollLast() {
        if (last == null) {
            return null;
        } else {
            return unlink(last);
        }
    }

    @Override
    public T getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public T getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public T peekFirst() {
        if (first == null) {
            return null;
        } else {
            return first.data;
        }
    }

    @Override
    public T peekLast() {
        if (last == null) {
            return null;
        } else {
            return last.data;
        }
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
    public Iterator<T> descendingIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {

        private int expectedModCount = modCount;
        private int lastRet = -1;
        private int cursor = 0;
        private Node<T> cur = first;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            checkForComodification();
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            T next = cur.data;
            cursor += 1;
            cur = cur.next;
            return next;
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            unlink(cur.prev);
            expectedModCount=modCount;
            cursor=lastRet;
            lastRet=-1;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        list.add("ab");
        list.add("bc");
        list.add("");
        list.add("11");
        Iterator<String> it = list.iterator();
        while (it.hasNext()){
            String v=it.next();
            System.out.println(";"+v);
            if(v.isEmpty()){
                it.remove();
            }
        }
        System.out.println(String.join(",",list));
    }
}
