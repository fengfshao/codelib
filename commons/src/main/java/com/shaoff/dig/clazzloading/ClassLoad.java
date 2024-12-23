package com.shaoff.dig.clazzloading;

/***
 * 何时发生类的加载
 * 1.jvm规范并未规定何时加载一个类，但是规定以下情况必须初始化一个类，此时必然要触发类的加载
 *      #1 静态方法，属性，构造函数
 *      #2 反射调用
 *      #3 作为其他要初始化的类的父类
 *      #4 虚拟机启动时指定的入口类
 *
 * 2.类的生命周期
 *      加载->验证->准备->解析->初始化->使用->卸载
 *      1-4又叫连接，解析阶段可以发生在初始化之后
 *
 */
public class ClassLoad {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(args.length);
//        this.getClass();
//        new ClassLoader
        bootstrapClassLoader();
        Thread.sleep(300000);
    }
    static void bootstrapClassLoader() {
        /***
         * 1.jvm参数标准配法　java -Xms128 -Dkey=value MainClass arg1 arg2
         * 重点　MainCLass后参数都将视为main函数参数，因此jvm参数必须写前面
         *
         * 2.此类加载器加载核心类，包括jre/lib下的几个jar包和jre/class目录，加载目录由参数-Xbootclasspath设置，
         * -Xbootclasspath:path(替换) /a:path 末尾追加，/p:path 往前插入
         * 替换时如果找不到类则会发生NoClassDefFoundError，java核心类无法覆盖，由于字节码有license信息
         */

        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("sun.boot.class.path").split(":").length);
    }

    static void extClassLoader(){
        /***
         * 1.顶级类加载器BootStrapClassLoader并没有对应的class文件,此加载器可能与jvm启动绑定在了一起
         * 2.ExtClassLoader是sun.misc.Launcher类的静态内部类，AppCLassLoader同样
         * 3.在Launcher类的构造函数中创建了局部变量ExtClassLoader，就是用于加载AppclassLoader,并创建
         * 该对象，然后设置当前线程的ContextClassLoader为该对象
         *
         */
    }

}
