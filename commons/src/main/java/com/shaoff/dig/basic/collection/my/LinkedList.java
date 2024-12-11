package com.shaoff.dig.basic.collection.my;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements List<T>, Deque<T> {

    private transient int size;
    private transient int modCount;
    private transient Node<T> first;
    private transient Node<T> last;

    private static class Node<T> {

        public Node(T ele, Node<T> p, Node<T> n) {
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
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public void addFirst(T t) {

    }

    @Override
    public void addLast(T t) {

    }

    @Override
    public boolean offerFirst(T t) {
        return false;
    }

    @Override
    public boolean offerLast(T t) {
        return false;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T pollFirst() {
        return null;
    }

    @Override
    public T pollLast() {
        return null;
    }

    @Override
    public T getFirst() {
        return null;
    }

    @Override
    public T getLast() {
        return null;
    }

    @Override
    public T peekFirst() {
        return null;
    }

    @Override
    public T peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    public void add(T ele) {
        linkLast(ele);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public void push(T t) {

    }

    @Override
    public T pop() {
        return null;
    }

    public T get(int pos) {
        return getNode(pos).data;
    }

    public T set(int pos, T newVal) {
        Node<T> old = getNode(pos);
        T oldVal = old.data;
        old.data = newVal;
        return oldVal;
    }

    public T remove(int pos) {
        return remove(getNode(pos));
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<T> node = first; node != null; node = node.next) {
            if ((o == null && node.data == null) || o != null && o.equals(node.data)) {
                return index;
            }
            index += 1;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    private void addBefore(Node<T> p, T ele) {
        Node<T> newNode = new Node<>(ele, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
        modCount++;
    }

    private void linkLast(T ele) {
        Node<T> newNode = new Node<>(ele, last, null);
        if (last == null) {
            last = first = newNode;
        } else {
            last.next = newNode;
            last = last.next;
        }
        size += 1;
        modCount += 1;
    }

    private T remove(Node<T> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount--;
        return p.data;
    }

    private Node<T> getNode(int pos) {
        return getNode(pos, 0, size() - 1);
    }

    private Node<T> getNode(int pos, int p, int r) {
        Node<T> node;
        if (pos < r || pos > r) {
            throw new IndexOutOfBoundsException();
        }
        if (pos < size() >> 2) {
            node = bm.next;
            for (int i = 0; i < pos; i++) {
                node = node.next;
            }
        } else {
            node = em;
            for (int i = size(); i > pos; i--) {
                node = node.prev;
            }
        }
        return node;
    }

    public void clear() {
        first = last = null;
        size = 0;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    private class LinkedListIterator implements Iterator<T> {

        private Node<T> cur = first.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return cur != em;
        }

        @Override
        public T next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T nextItem = cur.data;
            cur = cur.next;
            okToRemove = true;
            return nextItem;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            LinkedList.this.remove(cur.prev);
            expectedModCount++;
            okToRemove = false;
        }
    }
}
