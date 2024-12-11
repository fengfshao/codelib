package com.shaoff.dig.generic;

class Food {

}

class Fruit extends Food {

}

class Orange extends Fruit {

}

//不支持型变
class Broker<T> {

    Object[] data = new Object[10];
    int size = 0;

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public T get() {
        return (T) data[size - 1];
    }

    public void add(T t) {
        data[size++] = t;
    }
}

class FruitBroker<T extends Fruit> {

    Fruit[] data = new Fruit[10];
}

class Person {

    void eat(Broker<Fruit> fruits) {

    }

    void eat(FruitBroker<Fruit> fruits) {

    }

    //引入通配符支持型变的，属于协变，客户端可以传入xxx<Orange>,Orange<Fruit,xxx<Orange> < xxx<Fruit>,就像eatFruit
    void eat1(FruitBroker<? extends Fruit> fruits) {

    }

    void eatFruit(Fruit fruit) {

    }

    //<? super Fruit>支持逆变,Fruit是Food的子类，Broker<Food> 是 Broker<? super Fruit>的子类
    //一般协变用来从泛型容器中取数据，逆变用来放数据到泛型容器
    void moveFruit(Broker<? extends Fruit> source, Broker<? super Fruit> dest) {
        while (!source.isEmpty()) {
            dest.add(source.get());
        }
    }
}

public class 型变 {

    public static void main(String[] args) {
        // 不支持型变
        // Broker<Fruit> b = new Broker<Orange>();
        // 数组默认是协变的
        Fruit[] arr = new Orange[3];
        Person p = new Person();
        Broker<Orange> orangeBroker = new Broker<>();
        // p.eat(orangeBroker);

        FruitBroker<Orange> orangeBroker1 = new FruitBroker<>();
        p.eat1(orangeBroker1);
        p.eatFruit(new Orange());

        Broker<? super Fruit> var1 = new Broker<Food>(); //逆变
//        Broker<? super Fruit> var1=new Broker<Orange>();
        Broker<? extends Fruit> var2 = new Broker<Orange>();//协变
        System.out.println(var2.getClass().getCanonicalName());
    }
}
