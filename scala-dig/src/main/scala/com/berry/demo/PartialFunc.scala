package com.berry.demo

import scala.util.Random

/**
 * 偏函数，部分应用函数
 *
 * @author leonardo
 * @since 2024/9/18
 *
 */
object PartialFunc {

  def sum3(a: Int, b: Int, c: Int): Int = {
    a + b + c
  }

  def main(args: Array[String]): Unit = {


    // 参数全部应用，进行了函数调用，交出返回值
    val f0: Int = sum3(1, 0, -1)
    println(f0.getClass)
    // 参数部分应用，生成了匿名函数的实例
    val f1 = sum3(1, _:Int, -1)
    println(f1.getClass)

    var more=1
    val addMore=(x:Int)=>x+more
    println(addMore(2))
    more=100
    println(addMore(2))

    Array(1,2).foreach{x:Int=>
      println(x)
    }
  }
}
