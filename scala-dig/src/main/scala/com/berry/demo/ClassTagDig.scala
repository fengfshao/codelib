package com.berry.demo

import com.berry.demo.bean.{Color, ColoredPoint, Point}
import scala.reflect.{ClassTag, classTag}

/**
 * ClassTag理解
 *
 * @author fengfshao
 * @since 2023/6/29
 *
 */
object ClassTagDig {

  private def mkArray[T: ClassTag](elems: T*): Array[T] = Array[T](elems: _*)
  private def mkArrayWithType[T](elems: T*)(implicit ctg: scala.reflect.ClassTag[T]): Array[T] = {
    ctg.newArray(elems.length)
  }

  def main(args: Array[String]): Unit = {
    //[I ，java无法通过泛型api无法创建原生类型数组
    println(mkArray(1, 2, 3))
    //[Lcom.berry.scaladig.demo.bean.Point
    println(mkArray[Point](new Point(1, 1), new ColoredPoint(1, 2, Color.RED)))

    /**
     * 1.见java中的Arrays，scala中的ClassTag功能类似TypeReference，但其参数传递是通过隐式参数，更加优雅
     * 2.使用ClassTag的泛型函数，会有一个ClassTag[T]的隐式参数，ClassTag传参，传的是编译器调用ClassTag.apply(T)的返回值
     */
    println(mkArray[Int](1, 2, 3)(ClassTag.Int))
    println(mkArray[String]("a", "b"))
    println(mkArray[Array[Int]](Array(1, 2), Array(3, 4)))

    /**
     * 自定义的隐式参数被编译器找到并传参了，那么该隐式参数如何定位的
     * 1. 通常寻找对象需要显示声明为implicit，寻找范围为当前作用域的val或def定义，或者是对应类型相关类型的伴生对象中
     * 2. 也可能是import中引入的，scala中预先import的有scala包/Predef/java.lang
     * 3. 更高级的宏定义等方式预先引入，如ClassTag
     */
    println(mkArrayWithType[Long](1L, 2L, 3L))
    println(mkArrayWithType[Point](new Point(1, 1)))
  }
}
