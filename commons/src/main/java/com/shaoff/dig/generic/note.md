1.数组是协变的
2.Java泛型是不可变的
3.Java提供了 <? extends >和<? super>实现了协变和抗变
Broker<? super Fruit> var1 = new Broker<Food>(); //逆变
Broker<? extends Fruit> var2 = new Broker<Orange>();//协变
4.协变用来从数据结构中取，抗变用来往数据结构中放