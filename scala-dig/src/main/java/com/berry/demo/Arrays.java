package com.berry.demo;

import com.berry.common.TypeReference;
import com.berry.demo.bean.Point;

/**
 * 数组泛型验证
 *
 * @author fengfshao
 * @since 2023/6/29
 */

public class Arrays {

    // Array声明时必需指定具体类型，无法new T[]
    // 可以通过instanceOf+分支语句实现，但运行时无法获取T的信息
    public static <T> T[] mkArray(T... elements) {
        // T[] data=new T[elements.length];
        return (T[]) new Object[]{elements};
    }

    public static <T> T[] mkArray(TypeReference<T> typeRef, T... elements) {
        // convert Type to Class<?>
        // https://stackoverflow.com/questions/38761897/convert-java-lang-reflect-type-to-classt-clazz
        T[] data = (T[]) java.lang.reflect.Array.newInstance((Class<?>) typeRef.getType(), elements.length);
        System.arraycopy(elements, 0, data, 0, elements.length);
        return data;
    }


    public static void main(String[] args) {
        // 数组底层是协变的
        Object[] data = new String[2];
        String[] a1 = {"abc"};
        Object[] a2=a1;
        // a2[0]=new Integer(17); 运行时会抛出异常 java.lang.ArrayStoreException

        // String类型数组，[Ljava.lang.String
        System.out.println(data);
        // Object类型数组，[Ljava.lang.Object
        System.out.println(mkArray("aa", "bb"));
        // String类型数组
        System.out.println(java.lang.reflect.Array.newInstance(String.class, 2));
        // 自定义Class类型
        System.out.println(java.lang.reflect.Array.newInstance(Point.class, 3));
        // 自定义Class类型
        System.out.println(mkArray(new TypeReference<String>(){},"aa","bb"));
        System.out.println(mkArray(new TypeReference<Integer>(){}, 1,3));
    }
}
