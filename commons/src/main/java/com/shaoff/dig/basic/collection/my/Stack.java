package com.shaoff.dig.basic.collection.my;

public class Stack<T> {

    private ArrayList<T> data = new ArrayList<>();

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public T top() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        return data.get(data.size() - 1);
    }

    public void push(T ele) {
        data.add(ele);
    }

    public T pop() {
        return data.remove(data.size() - 1);
    }

}
