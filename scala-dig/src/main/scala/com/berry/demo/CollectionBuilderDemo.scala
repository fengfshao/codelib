package com.berry.demo

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
 * collection builder
 * <a href="https://blog.csdn.net/chengmi1845/article/details/100698452/">The Architecture of Scala Collection</a>
 *
 * @author leonardo
 * @since 2024/9/24
 *
 */

object CollectionBuilderDemo {

  implicit def canBuildFrom[T](implicit t: ClassTag[T]): CanBuildFrom[Array[_], T, Set[T]] =
    new CanBuildFrom[Array[_], T, Set[T]] {
      def apply(from: Array[_]) =  Set.newBuilder[T]
      def apply() = Set.newBuilder[T]
    }

  def main(args: Array[String]): Unit = {
    val s="123"
    val buf = new ArrayBuffer[Int]
    val newBuilder: mutable.Builder[Int, Array[Int]] = buf.mapResult(_.toArray)
    newBuilder += 1
    newBuilder += 2
    val arr=newBuilder.result()

    // 隐式参数显示声明后
    val myNumbers  =arr.map(_+1)
    //Predef.intArrayOps(arr).map(_ + 1)(Array.canBuildFrom)

    // 伴生类中增加隐式转换
    println(myNumbers.getClass)
    // class scala.collection.immutable.Set$Set

    val mapNums=Map("a" -> 1, "b" -> 2) map { case (x, y) => y} // result in List[Int]
    println(mapNums.genericBuilder)
  }
}
