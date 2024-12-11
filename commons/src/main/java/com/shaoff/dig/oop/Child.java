package com.shaoff.dig.oop;

class Something{
    public Something(){
        System.out.println("constructing something");
        System.out.println(this);
    }
}

/***
 * 1.继承中，子类究竟继承了什么，是继承了非私有变量吗
 * 继承中，与其用继承两字，更准确地应当说子类对象包含对父类对象的引用，Java的继承是对于类而言的，而非一个对象继承了另一个对象
 * 就是说，创建子类时，会先创建其直接父类的对象，然后再创建子类对象，这个父类对象的成员与方法在子类中通过super进行访问，同时
 * 依然受到访问权限的约束
 * 2.上文说到，子类包含整个父类对象的引用，那么在子类中，如何访问父类的成员变量与方法呢？
 * 具体来说，这些信息表示在字节码中，字节码中只包含有符号引用，在解析和方法执行前解析为直接引用，对于字段的符号引用，在类加载时进行
 * 解析，首先从字段所属的类查找，然后再按照继承或实现关系从下往上找，最后还会进行访问权限的验证。
 *   对于可重写的虚方法来说，即Invokevirtual调用的非final方法，其解析是发生在运行时，因为编译后的字节码中，表示被调用的方法用的都是
 *   静态类型，字面量相同，而在方法调用阶段会进行动态分派，按照从下往上的顺序寻找描述符和简单名称都相同的方法。
 *   对于其他方法如静态，私有，final等，会在类加载就完成解析
 *
 * 3.重载与重写
 * 重载是指根据引用的静态类型，即函数名和参数列表，发生在同一个类中，在编译后就能确定实际的方法，具体来说在字节码中用不同的字面量表示，
 * 对于重写，发生在继承关系中，是指调用非虚方法时，根据对象动态类型来确定调用方法，具体来说，对于发生重写的方法调用，其产生的字面量相同，
 * 调用者的动态类型未知，故在类加载时的解析阶段无法确定其直接引用，需要等到方法执行前的动态分派阶段根据实际类型从下往上查找方法，转换为直接引用。
 */
class Base{
    public int a=1;
    private Something b;
    private int c=-10;
    public Base(){
        b=new Something();
    }
    protected Base(int c){
        this.c=c;
    }

    public Something getPrivate(){
        return b;
    }
    public int getPrivateInt(){
        return c;
    }
}
public class Child extends Base{
    public Child() {
//        super(2);
    }

    public int getBasePrivate() {
//        return super;
        return -1;
//    }
//    public Base getBase(){
////        return super;
//    }
    }
    public static void main(String[] args) {
        Child c=new Child();
        Base base=c;
        if(base instanceof Base){
            System.out.println("base");
        }
        if(base instanceof Child){
            System.out.println("child");
        }
        Child d= (Child) base;
        System.out.println(c.getPrivateInt());
    }

}
