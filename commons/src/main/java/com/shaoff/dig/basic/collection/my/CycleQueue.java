package com.shaoff.dig.basic.collection.my;

public class CycleQueue<T> {
    private static final int DEFAULT_CAPACITY=10;
    private T[] items;
    private int front;
    private int end; //表示最后一个元素的下一个位置，不是last

    public CycleQueue() {
        this(DEFAULT_CAPACITY);
    }

    public CycleQueue(int capacity) {
        items= (T[]) new Object[capacity+1];
    }

    public int size(){
        return ((end-front)+items.length)%items.length;
    }
    public boolean isEmpty(){
        return front==end;
    }

    private boolean isFull(){
        return (end+1)%items.length==front;
    }
    public void offer(T ele){
        if(isFull()){
            throw new ArrayIndexOutOfBoundsException();
        }
        items[end]=ele;
        end=(end+1)%items.length;
    }

    public T poll(){
        if(isEmpty())
            throw new ArrayIndexOutOfBoundsException();

        T ele=items[front];
        front=(front+1)%items.length;
        return ele;
    }
}
