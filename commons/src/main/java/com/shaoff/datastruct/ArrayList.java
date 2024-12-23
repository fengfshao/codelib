package com.shaoff.datastruct;

public class ArrayList<T> {

    private int theSize;
    private T[] theItems;

    public ArrayList() {
    }

    public int size() {
        return theSize;
    }

    public T get(int pos) {
        if (pos < 0 || pos >= theSize)
            throw new ArrayIndexOutOfBoundsException();
        return theItems[pos];
    }

    public T set(int pos, T ele) {
        if (pos < 0 || pos >= theSize)
            throw new ArrayIndexOutOfBoundsException();
        T old = theItems[pos];
        theItems[pos] = ele;
        return old;
    }

    public boolean add(T ele) {
        add(ele);
        return true;
    }

    public void add(int pos, T ele) {
        if (theItems.length == theSize) {
            ensureCapacity(size() * 2 + 1);
        }
        for (int i = theSize; i > pos; i--)
            theItems[i] = theItems[i - 1];
        theItems[pos] = ele;
        theSize++;
    }

    public T remove(int pos) {
        T item = theItems[pos];
        for (int i = pos; i < theSize - 1; i++) {
            theItems[i] = theItems[i + 1];
        }
        theSize--;
        return item;
    }

    private void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize) return;

        T[] old = theItems;
        theItems = (T[]) new Object[newCapacity];
        for (int i = 0; i < size(); i++)
            theItems[i] = old[i];
    }
}
