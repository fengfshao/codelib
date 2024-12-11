package com.shaoff.dig.generic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Pair<U, V> {
    static int a=0;
    U first;
    V second;

    public Pair(U first, V second){
        this.first = first;
        this.second = second;
    }

    public Pair() {
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        List<U> ls=new ArrayList();
        if(first instanceof String){
            return first.toString();
        }else if(first instanceof Integer){
            return Integer.parseInt(first.toString())+":";
        }else{
            return first.getClass().getSimpleName();
        }
    }
}

public class 擦除 {
    static Pair<Integer,String> pair1=new Pair<>(1,"abc");
    public static void main(String[] args) throws Exception {
        Pair<Integer,String> pair2=new Pair<>(1,"abc");
        Type type1=pair1.getClass().getDeclaredField("first").getGenericType();
        Type type2=pair2.getClass().getDeclaredField("first").getGenericType();
        System.out.println(type1);
        System.out.println(type2);
//        Pair<String,String> pair2=new Pair<>("def","abc");
    }
}
