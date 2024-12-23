//package com.shaoff.datastruct;
//
//import java.util.List;
//
//public class Heap<E extends Comparable<E>> {
//    ArrayList<E> data=new ArrayList<>();
//
//    public Heap(){ }
//
//    public int size(){
//        return data.size();
//    }
//
//    public void add(E val){
//        data.add(val);
//        shiftup(data.size()-1);
//    }
//
//    public E remove(){
//        E root=data.get(0);
//        data.set(0,data.remove(data.size()-1));
//        shitfdown(0);
//        return root;
//    }
//
//    public Heap(List<E> data){
//        this.data.addAll(data);
//        for(int i=parent(size()-1);i>=0;i--)
//            shitfdown(i);
//    }
//
//    private int left(int p){
//        return 2*p+1;
//    }
//    private int right(int p){
//        return 2*p+2;
//    }
//    private int parent(int c){
//        if(c==0) return -1;
//        return (c-1)/2;
//    }
//    //i前的元素组成堆，i表示调整堆使得i处的元素也符合堆
//    private void shiftup(int i) {
//        int cur=i;
//        int parent=parent(cur);
//        E keyVal=data.get(cur);
//        while (cur>0&&keyVal.compareTo(data.get(parent))<0){
//            data.set(cur,data.get(parent)); //pull parent value down
//            cur=parent;
//            parent=parent(cur);
//        }
//        data.set(cur,keyVal);
//    }
//
//    //i后的元素组成堆，i表示调整堆使得i处的元素也符合堆
//    private void shitfdown(int i) {
//        int end = size();
//        int cur = i;
//        int child = left(cur);
//        E keyVal = data.get(cur);
//
//        while (child < end) {
//            if (child + 1 < end && data.get(child).compareTo(data.get(child + 1)) < 0) {
//                child = child + 1;
//
//                if (keyVal.compareTo(data.get(child)) < 0)
//                    break;
//                data.set(cur, data.get(child));
//                cur = child;
//                child = left(cur);
//            }
//            data.set(cur, keyVal);
//        }
//    }
//
//}
