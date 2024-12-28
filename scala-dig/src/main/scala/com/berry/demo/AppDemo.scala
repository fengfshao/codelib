package com.berry.demo

import scala.util.Random

/**
 * App特质使用
 *
 * @author fengfshao
 * @since 2023/6/20
 *
 */

object AppDemo  {
  //println(s"hello ${args(0)}")

  val f1: (Int => Int) = { x =>
    {
      println("val")
      x + 1
    }
  }

  def f2: (Int => Int) = {
    println("def")
    (x => x + 1)
  }

  def f3():Int={
    Random.nextInt(10)
  }

  def f4:Int={
    Random.nextInt(10)
  }

  def main(args: Array[String]): Unit = {
    Array(1,2,3).toStream.map(f3)
    println("finished")
  }

  def f3(e:Int):Int={
    println("once")
    e
  }
}
