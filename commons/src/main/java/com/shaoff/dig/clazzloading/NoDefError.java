package com.shaoff.dig.clazzloading;

/**
 * Author: shaoff
 * Date: 2020/6/17 14:10
 * Package: classloading1
 * Description:
 * ClassNotFoundException与NoClassDefFoundError的区别
 * https://stackoverflow.com/questions/34413/why-am-i-getting-a-noclassdeffounderror-in-java
 *
 * 简单说
 * 1.ClassNotFound 是类加载时类加载器无法加载到类时，如无法从classpath找到类时
 * 2.NoClassDefFoundError是虚拟机在获取class的数据结构时，即class元信息时元信息不存在造成的
 *   通常发生在：类在第一次使用时ClassNotFound或是初始化失败，然后再次使用时不会尝试初始化而是直接抛出该Error
 *   也可能发生在，编译器类存在，运行时类不存在，如maven package时过滤或shade了某些类
 */
class JustAClass{
    static int c= error_method();

    static int error_method(){
        throw new RuntimeException("hh");
    }
}

class JustBClass{
    static JustAClass c=new JustAClass();
}

public class NoDefError {
    public static void main(String[] args) throws Exception {
        try{
            JustAClass b=new JustAClass();
        }catch (Throwable t){
//            t.printStackTrace();
        }
        JustAClass a= new JustAClass();
    }
}
