package com.berry.scalaProgramming

import java.io.{File, PrintWriter}
import scala.util.Random

/**
 * scala编程第三版6-9章调试
 *
 * @author leonardo
 * @since 2024/10/11
 *
 */
// 主构造函数中参数加val/var和不加的区别 https://blog.csdn.net/weixin_53107837/article/details/114596419
// 混入Ordered特质，增加排序功能
class Rational(n: Int, d: Int) extends Ordered[Rational] {
  require(d != 0)
  private val g = gcd(n, d)
  val number = n / g
  val denom = d / g
  def this(n: Int) = this(n, 1) // 辅构造函数
  override def toString: String = number + "/" + denom
  def +(that: Rational) = new Rational(number * that.denom + that.number * denom, denom * that.denom)
  def +(that: Int) = new Rational(number + that * denom, denom)
  def *(that: Rational) = new Rational(number * that.number, denom * that.denom)
  def *(that: Int) = new Rational(number * that, denom)
  def lessThan(that: Rational) = this.number * that.denom < that.number * this.denom
  def max(that: Rational) = if (lessThan(that)) that else this

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  /* idea生成equals方法，能够==判断 */
  private def canEqual(other: Any): Boolean = other.isInstanceOf[Rational]
  override def equals(other: Any): Boolean = other match {
    case that: Rational =>
      that.canEqual(this) && g == that.g &&
        number == that.number &&
        denom == that.denom
    case _ => false
  }
  override def hashCode(): Int = {
    val state = Seq(g, number, denom)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
  override def compare(that: Rational): Int = {
    this.number * that.denom - that.number * this.denom
  }
}

object Demo extends App {
  // 函数式编程风格的Rational
  val c = new Rational(1, 2) * new Rational(2, 3)
  val a = c + 3
  implicit def int2Rational(v: Int) = new Rational(v)
  val b = 3 + c
  assert(a == b)

  // 函数是一等公民，可以作为变量传递，放入集合等
  val f1: () => Int = () => Random.nextInt()
  val functionList = List.empty[() => Int]
  val newList = f1 :: (() => Random.nextInt(10)) :: functionList

  def getRandom: Long = System.currentTimeMillis() % 10
  val f2: () => Long = getRandom _ // 方法和函数间转换

  // 引用自由变量的函数或一段代码称为开放项，其在运行时需要完成对自由变量的捕获，从而关闭这个开放项，称为闭包
  // scala中的外部变量是可变的，java中不可变
  var more: Int = 0
  val f = (a: Int) => a + more
  assert(f(1) == 1)
  more = 2
  assert(f(1) == 3)

  // 柯里化函数有多个参数列表
  def curriedSum(x: Int)(y: Int) = x + y * y
  val sum1: Int => Int = curriedSum(2)
  val sum2: Int => Int = curriedSum(_: Int)(2)
  assert(sum2(1) == 5)
  // 只传入一个参数时可以用花括号代理小括号，使得看着更像是对控制结构的扩展
  def withPrinter(file: File)(op: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
  withPrinter(new File("a.txt")) { pw =>
    pw.println("...")
    pw.println("===")
    pw.println("---")
  }

  // 比较Rational
  new Rational(1,2) >= new Rational(1,3)
}
