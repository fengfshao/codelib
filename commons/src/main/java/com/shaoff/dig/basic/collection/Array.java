package com.shaoff.dig.basic.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

class SomeThing{
    int a=10;
}

public class Array {

    public static void testArayCopy(){
        SomeThing[] someThings=new SomeThing[5];
        for(int i:Arrays.asList(1,2,3,4,5) ){
            someThings[i-1]=new SomeThing();
        }
        SomeThing[] someThings1=Arrays.copyOf(someThings,someThings.length+1);
        for(int i:Arrays.asList(0,1,2,3,4))
            System.out.println(someThings[i]==someThings1[i]);
    }
    public static void main(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "1");
        map.put("cbjs", "1");
        map.put("ljfkdls", "1");
        map.put("d", "1");
        map.put("efjsk", "1");
        map.put("g", "1");
        map.put("f", "1");

        for (Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            System.out.println(k + ":" + v);
        }

        testArayCopy();
        ArrayList<String> list = new ArrayList<>();
        System.out.println(10&7);
        System.out.println(10%7);
        //根据索引插入，更新时，索引必须小于size
/*
        list.set(1024, "def");
        list.add(1024, "abc");
*/
    }
}
