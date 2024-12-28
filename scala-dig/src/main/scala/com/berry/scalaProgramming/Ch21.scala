package com.berry.scalaProgramming

/**
 * scala编程第三版21章本地调试
 *
 * @author leonardo
 * @since 2024/10/23
 *
 */
class PreferredPrompt(val preference: String)
object Greeter {
  def greet(name: String)(implicit prompt: PreferredPrompt): Unit = {
    println("Welcome, " + name + ". The system is ready.")
    println(prompt.preference)
  }
}
object JoesPrefs {
  implicit val prompt = new PreferredPrompt("Yes, master> ")
}
object Ch21 {

  def main(args: Array[String]): Unit = {

    /*
     * 隐式转换一般有一个S=>T的函数类型隐式值来定义，为了避免混乱，编译器遵循如下规则
     * 1. 作用域规则，作用于相关上下文或是源类型和目标类型伴生类定义中
     * 2. 无歧义规则，如果有多个隐式转换可以通过，则编译器会拒绝选择
     * 3. 单一调用规则，只会尝试一个隐式操作，不会嵌套式调用
     * 4. 显示先行，如果代码的类型检查通过，不会尝试任何隐式操作
     *
     * */
    implicit def stringWrapper(s: String, b: Int): IndexedSeq[Char] = return new IndexedSeq[Char] {
      override def length: Int = s.length
      override def apply(idx: Int): Char = s.charAt(idx)
    }
    val str1 = "axjlk"
    str1.slice(2, 3)

    // 隐式参数实现隐式操作
    Greeter.greet("joe")(JoesPrefs.prompt)

    def maxListImpParm[T](elements: List[T])(implicit order: T => Ordered[T]): T = {
      elements match {
        case List() => throw new IllegalArgumentException("empty list.")
        case List(x) => x
        case x :: rest =>
          // 隐式参数不但会被隐式传参，参数还会在方法体中被隐式应用
          val maxRes = maxListImpParm(rest) // 此处隐式传参进行递归调用
          if (x > maxRes) x else maxRes // 这里隐式转换将x转为了Ordered[T]
      }
    }

    // T <% Ordered[T] 是视界的语法，上界<:中要求A是B，视界要求A可以当作B用，这就有了隐式转换的空间
    // 不过视界的语法后续将被移除
    def maxList[T <% Ordered[T]](elements: List[T]): T = ???
    implicit val ppOrder: PreferredPrompt => Ordered[PreferredPrompt] = x =>
      new Ordered[PreferredPrompt] {
        override def compare(that: PreferredPrompt): Int = x.preference compare that.preference
    }
    val res = maxListImpParm[Int](List.apply(1, 2, 3))(x => scala.Predef.intWrapper(x))
    val res2 = maxListImpParm(List(new PreferredPrompt("abc"), new PreferredPrompt("hcc")))
    println(res2.preference)

    val p1 = new PreferredPrompt("1")
    val p2 = new PreferredPrompt("12")
    val compareRes: Boolean = p2 > p1 // 其实是 ppOrder.apply(p2).>(p1)
  }

}
