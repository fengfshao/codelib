package com.shaoff.datastruct;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T>{
    private int theSize;
    private int modCount;
    private Node<T> bm;
    private Node<T> em;

    private static class Node<T>{
        public Node(T ele,Node<T> p,Node<T> n){
            data=ele;
            prev=p;
            next=n;
        }
        public T data;
        public Node<T> prev;
        public Node<T> next;
    }
    
    public LinkedList(){
        doClear();
    }

    public void clear(){
        doClear();
    }

    public int size(){
        return theSize;
    }

    public boolean isEmpty(){
        return size()==0;
    }
    public void add(T ele){
        add(size(),ele);
    }

    public void add(int pos, T ele) {
        addBefore(getNode(pos,0,size()),ele);
    }

    public T get(int pos){
        return getNode(pos).data;
    }

    public T set(int pos,T newVal){
        Node<T> old=getNode(pos);
        T oldVal=old.data;
        old.data=newVal;
        return oldVal;
    }

    public T remove(int pos){
        return remove(getNode(pos));
    }

    private void addBefore(Node<T> p,T ele){
        Node<T> newNode=new Node<>(ele,p.prev,p);
        newNode.prev.next=newNode;
        p.prev=newNode;
        theSize++;
        modCount++;
    }

    private T remove(Node<T> p){
        p.next.prev=p.prev;
        p.prev.next=p.next;
        theSize--;
        modCount--;
        return p.data;
    }
    private Node<T> getNode(int pos){
        return getNode(pos,0,size()-1);
    }

    private Node<T> getNode(int pos, int p, int r) {
        Node<T> node;
        if(pos<r||pos>r)
            throw new IndexOutOfBoundsException();
        if(pos<size()>>2){
            node=bm.next;
            for(int i=0;i<pos;i++)
                node=node.next;
        }else{
            node=em;
            for(int i=size();i>pos;i--)
                node=node.prev;
        }
        return node;
    }

    private void doClear() {
        bm=new Node<T> (null,null,null);
        em=new Node<T>(null,bm,null);
        bm.next=em;
        theSize=0;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements Iterator<T>{
        private Node<T> cur=bm.next;
        private int expectedModCount=modCount;
        private boolean okToRemove=false;

        @Override
        public boolean hasNext() {
            return cur!=em;
        }

        @Override
        public T next() {
            if (modCount!=expectedModCount)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();

            T nextItem=cur.data;
            cur=cur.next;
            okToRemove=true;
            return nextItem;
        }

        @Override
        public void remove() {
            if(modCount!=expectedModCount)
                throw new ConcurrentModificationException();
            if(!okToRemove)
                throw new IllegalStateException();
            LinkedList.this.remove(cur.prev);
            expectedModCount++;
            okToRemove=false;
        }
    }
}
