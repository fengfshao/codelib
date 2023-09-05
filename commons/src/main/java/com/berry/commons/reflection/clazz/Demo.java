package com.berry.commons.reflection.clazz;

import com.berry.bean.Student;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 反射类的记录
 *
 * @author fengfshao
 * @since 2023/7/27
 */

public class Demo {

    public static void main(String[] args) {
        getFields();
    }

    static void getFields() {
        Student stu = new Student("a", 0.1);
        Field[] f1 = stu.getClass().getDeclaredFields(); // 获取本类声明的所有字段，包括非public，不包括父类的
        Field[] f2 = stu.getClass().getFields(); // 获取本类及父类所有的public字段
    }

    /**
     * instanceof 和 isAssignableFrom 都可以判断一个对象是否是某个类或接口的实例
     * instanceof在编译时需要确定的类型，isAssignableFrom判断的类型信息是运行时确定的，是动态可变的<br>
     * https://stackoverflow.com/questions/496928/what-is-the-difference-between-instanceof-and-class-isassignablefrom
     */
    static void instanceOfIsAssignableFrom() {
        List<String> a = new ArrayList<>();
        List<String> b = new LinkedList<>();

        if (a instanceof ArrayList) {
            if (a instanceof List) {
                System.out.println("will print this.");
            }
        }

        if (b.getClass().isAssignableFrom(a.getClass())) {
            if (b.getClass().isAssignableFrom(CopyOnWriteArrayList.class)) {
                System.out.println("will print this.");
            }
        }
    }
}
