package com.berry.scalaProgramming

/**
 * scala编程第三版1-5章调试
 *
 * @author leonardo
 * @since 2024/10/6
 *
 */

/**
 * 延迟初始化的标记特质DelayedInit，如果实现该特质，编译器将主构造函数中的代码放在delayedInit$body中
 * 用户可以在delayedInit方法中控制body的调用顺序
 *
  */
class TestA extends DelayedInit{
  println("Aconstructor called.")
  override def delayedInit(body: => Unit): Unit = {
    // 初始化代码在函数x中
    println("before constructorA")
    body
  }
}
class TestB extends DelayedInit{
  println("Bconstructor called.")
  override def delayedInit(x: => Unit): Unit = {
    // 初始化代码在函数x中
    println("before constructorB")
  }
}

/**
 * application特质，无需使用main函数，其实现直借助了DelayedInit的功能，将主构造器中的代码放在了initCode中，
 * 并在特质中的main函数中调用，参考 <a href="https://blog.csdn.net/wang2leee/article/details/132868793"> App trait原理</a>
 *
  */
object AppDemo extends App {
  val argArr = args
  println("arrArg" + argArr.mkString("Array(", ", ", ")"))
}

object Ch1_5 {
  def main(args: Array[String]): Unit = {
    // Unit类型的唯一单例，()
    val obj: Unit = ()

    // 借助DelayedInit延迟初始化
    // TestA延迟初始化完成，TestB因为没调用body所以没有执行初始化
    val objA=new TestA()
    val objB=new TestB()

    // scala的操作符其实只是普通的方法，一般左操作数是调用方，右操作数是参数，除非符号以:结尾
    // 同时，普通的方法也可以像操作符那样使用
    val sum=1.+(2)
    val subStr="sfhe" substring 2
    val newList1="a"::List("b","c")
    val newList2=List("b","c").::("a")
    assert(newList1==newList2)

    // 三引号的字符串字面量，可以避免双引号时引入转义
    val jsonStr=
      """
        |"w":'abc'
        |""".stripMargin

    // scala的符号字面量，仅是string的包装
    val symbol: Symbol ='sfjl
    assert(symbol==Symbol("sfjl"))

    // scala中的==，相当于是java中Objects.equals调用，会使用equals判断对象的相等性
    // 通过eq可以仅判断引用是否相同
    val str1="abc"
    val str2=new String("abc")
    assert(str1==str2)
    assert(!(str1 eq str2))
    assert(!(null eq str2 )) // 不会发生空指针异常
    assert(str1 ne str2) // eq的反义词

    // 隐式转换的包装，增加了很多可调用方法
    val intVal: Int ="123".toInt // StringOps
    val longVal=intVal.toBinaryString // RichInt


  }
}
