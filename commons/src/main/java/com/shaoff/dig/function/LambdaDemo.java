package com.shaoff.dig.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class LambdaDemo {
    public static void main(String[] args) {

        /*
         * lambda表达式可以替换匿名内部类，其使用范围限定在只有一个抽象方法的接口
         * java编译后lambda表达式不会生成匿名内部类，底层是借助invokeDynamic实现
         * */
        List<Integer> nameList = new ArrayList<>();
        Collections.sort(nameList, (a, b) -> b - a);

        Executors.newCachedThreadPool().submit(() -> {
            System.out.println("hello world");
        });

        // 函数式接口，只有一个抽象方法
        // Predict，进行bool值判断
        List<String> valueList = new ArrayList<>();
        valueList.stream().filter(String::isEmpty);
        // Function 进行转换
        valueList.stream().map(String::toUpperCase);
        // Consumer 执行一个操作，无返回值
        valueList.forEach(System.out::println);
        // Supplier 提供一个返回值
        nameList.stream().collect(ArrayList::new, (objects, e) -> objects.add(e), ArrayList::addAll);
    }
}
