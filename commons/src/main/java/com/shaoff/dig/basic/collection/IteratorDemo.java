//package com.shaoff.dig.basic.collection;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * 验证迭代器
// *
// * @author leonardo
// * @since 2024/9/8
// */
//
//public class IteratorDemo {
//
//    private static class MyCollection implements Iterable<String> {
//
//        @Override
//        public Iterator<String> iterator() {
//            List<String> abc = Arrays.asList("xx", "yz");
//            Iterator<String> p=abc.iterator();
//            return new Iterator<String>() {
//                @Override
//                public boolean hasNext() {
//                    return p.hasNext();
//                }
//
//                @Override
//                public String next() {
//                    System.out.println("next called!");
//                    return p.next();
//                }
//            };
//        }
//    }
//
//    public static void testForeach(){
//        MyCollection c = new MyCollection();
//
//        // foreach 循环会编译为对迭代器的调用
//        for (String str : c) {
//            System.out.println("cur: " + str);
//        }
//    }
//
//    public static void testCurrentMod(){
//        List<Integer> nums = new ArrayList<>();
//        nums.add(1);
//        nums.add(3);
//        nums.add(5);
//        // 一边遍历一边有插入删除等操作，会抛出java.util.ConcurrentModificationException异常
///*        for(Integer num:nums){
//            nums.add(num + 1);
//        }*/
//
//        // 如果集合的实现不做这个限制，可能会导致很多奇怪的错误，如这段无法停止的代码，最终会耗尽内存
//        com.shaoff.myds.ArrayList<Integer> myList = new com.shaoff.myds.ArrayList<>();
//        myList.add(1);
//        myList.add(3);
//        myList.add(5);
//        for(Integer num:myList){
//            System.out.println(num+1);
//            myList.add(num + 1);
//        }
//        System.out.println(myList);
//    }
//    public static void main(String[] args) {
//        testCurrentMod();
//    }
//}
