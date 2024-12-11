package com.shaoff.dig.oop;

/**
 * 关于继承的一些总结
 *
 * @author fengfshao
 */
public class ExtendsDemo {

    private static class Base {

        private static int a = 1;
        private int b = 2;

        static {
            System.out.println("Base static block " + a);
        }

        {
            System.out.println("Base init block " + b);
        }

        public Base() {
            System.out.println("Base constructor " + b);
        }
    }

    private static class Child extends Base {

        private static int a = 3;
        private int b = 4;

        static {
            System.out.println("Child static block " + a);
        }

        {
            System.out.println("Child init block " + b);
        }

        public Child() {
            System.out.println("Child constructor " + b);
        }
    }

    private static class Base2 {

        public Base2() {
            test();
        }

        public void test() {
        }
    }

    private static class Child2 extends Base2 {

        private int a = 123;

        public Child2() {
        }

        public void test() {
            System.out.println(a);
        }
    }

    private static class Base3 {

        public int a = 1;
    }

    private static class Child3 extends Base3 {

        public int a = 2;
    }

    private static class Base4{
        /*静态属性可以被继承*/
        static String a="a";
        /*静态方法可以被继承*/
        static void f(){
            System.out.println("function f");
        }
        static String b="b";
    }

    private static class Child4 extends Base4{
        static String b="b1";
    }

    public static void main(String[] args) {
        /*
         关于java中类的初始化顺序：
         1初始化父类静态变量和静态代码块
         2初始化子类静态变量和静态代码块
         3初始化父类实例变量和代码块
         4初始化父类构造器
         5初始化子类实例变量和代码块
         6初始化子类构造器
         */
        Base b = new Child();

        /* 父类的构造函数中不应调用可能被重写的方法，或非private方法 */
        new Child2(); // 访问了未初始化的成员变量

        /* 只有实例方法是动态的绑定的，成员变量静态绑定，访问到父类还是子类的成员变量取决于引用类型 */
        Child3 ref1 = new Child3();
        Base3 ref2 = ref1;
        System.out.println(ref1.a);
        System.out.println(ref2.a);

        /* 静态字段和方法也可以继承 */
        System.out.println(Child4.a);
        Child4.f();
    }
}
