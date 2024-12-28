package com.berry.demo

/**
 * 隐式参数与隐式转换
 *
 * @author fengfshao
 * @since 2023/7/4
 *
 */
object Implicits {

  implicit final class MyToJson[A](private val self: A) extends AnyVal {
    @inline def toJson: String = ScalaJsonUtil.toJson(self)
  }

  def sum(strList: List[String])(implicit f: Formatter[String, Int]): Int = {
    strList.map(f.format).sum
  }

/*
  def sum2(longList: List[Long])(implicit f: Formatter[Long, Int]): Int = {
    longList.map(f.format).sum
  }
*/

  def main(args: Array[String]): Unit = {
    // 自动隐式参数传参
    println(sum(List("321", "45")))
    // 触发@implicitNotFound中的提示
    //println(sum2(List(32L,49L)))

    // 自动隐式转换
    println("abc".toJson)
    println(Map("k1"->1).toJson)
    val c="ab".eq("c")
  }
}

@scala.annotation.implicitNotFound(msg = "Cannot found formatter from ${A} to ${B}")
trait Formatter[-A, +B] {
  def format(in: A): B
}

object Formatter {
  // 需要声明为implicit，才能被编译器定位
  implicit val STR2INT: Formatter[String, Int] = new Formatter[String, Int] {
    override def format(in: String): Int = in.toInt
  }

  implicit val Long2INT: Formatter[Long, Int] = new Formatter[Long, Int] {
    override def format(in: Long): Int = in.toInt
  }

  //def apply(): Formatter[String, Int] = STR2INT
}
