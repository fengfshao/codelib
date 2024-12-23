package com.shaoff.dig.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: shaoff
 * Date: 2020/6/1 11:07
 * Package: box
 * Description:
 */
public class BoxDemo {
    static int a=1;
    static Integer b=2;
    static List<Integer> arr=new ArrayList<>();
    static void cache(){
        //使用Integer包装类时，通常使用Integer.valueOf方法构造，其中有常量池，默认有-128-127之间的数
        //使用私有静态内部类实现，当类被初始化时初始缓存
        Integer i=Integer.valueOf(1);
    }

    static void dirtyWork(){
        //自动装箱和拆箱是通过编译器增加字节码实现的
        //当有类型转换时，调用valueOf或是intValue方法
        //基本类型和封装类型比较时，进行自动拆箱，即比较两个基本类型的值
    }

    static void compareBox(){
        //封装类型应该使用equals方法比较，==仅仅是比较引用
    }


    public static void main(String[] args) {
        arr.add(a); //编译器自动增加了Integer.valueOf(a)的调用
        arr.add(b);
        int c=b+1; //编译器自动增加了b.intValue()的调用

        boolean refEq=a==b; //这里自动拆箱
        Integer d=2;
        boolean refEq2=b==d; //直接比较的引用，由于cache,两者一致
        System.out.println(refEq2);
        System.out.println(d==new Integer(2)); // 比较引用，不用的对象结果不同
        Integer e=new Integer(2);
        System.out.println(b==e); //直接比较引用，两个对象
        System.out.println(b.equals(e)); //true

        Integer a=1;
        Integer b = new Integer(1);
        System.out.println(a==b);
    }

    /**
     * 验证一下自动拆装遇到null时的情况
     */
    public static void caseNull(String[] args) {
        Map<String, Boolean> m = new java.util.HashMap<String, Boolean>();
        m.put("aaa", true);

        Boolean b = m.get("bbb");
        if (b != null) {
            System.out.println(b.booleanValue());
        }
        // 这里果断会空指针异常
        boolean c = m.get("bbb");
        System.out.println(c);
    }
}
