package com.berry.demo

/**
 * 更多高级for循环
 *
 * @author fengfshao
 * @since 2023/6/20
 *
 */
object ForLoop {
  def main(args: Array[String]): Unit = {
    for (i <- 1 to 3; j <- 1 to 3) print(f"${10 * i + j}%3d")
    println()
    for (i <- 1 to 3; j <- 1 to 3 if i != j) print(f"${10 * i + j}%3d")
    println()
    for (i <- 1 to 3; from = 4 - i; j <- from to 3) print(f"${10 * i + j}%3d")
    println()
    for (i <- 0 to 10; j = 10 - i) { print(f"$j%2d") }
    println()
    val numbers = for (i <- 0 until 10) yield i % 3
    println(numbers)

    val str=Some("111")
    for (c<-str)  {println(c)}
  }

  val f1:Double=>Option[Double]=(x:Double)=>Some(0)
  val f2:Double=>Option[Double]=(x:Double)=>Some(0)

  val f3:Double=>Option[Double]=(x:Double)=>f1(x).flatMap(f2)

}
