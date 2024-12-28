package com.berry.scalaProgramming

import Element.elem

import scala.collection.mutable.ArrayBuffer

/**
 * scala编程第三版10-14章本地调试
 *
 * @author leonardo
 * @since 2024/10/12
 *
 */
abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length

  def above(that: Element): Element = elem(this.contents ++ that.contents)
  def beside(that: Element): Element = elem((this.contents zip that.contents).map(pair => pair._1 + pair._2))
  override def toString: String = contents.mkString("\n")
}

object Element {
  def elem(contents: Array[String]): Element = new ArrayElement(contents)
  def elem(char: Char, width: Int, height: Int): Element = new UniformElement(char, width, height)
  def elem(line: String): Element = new LineElement(line)

  private class ArrayElement(val contents: Array[String]) extends Element
  private class LineElement(s: String) extends Element {
    override val contents: Array[String] = Array(s)
    override val height = 1
    override val width: Int = s.length
  }
  private class UniformElement(ch: Char, override val height: Int, override val width: Int) extends Element {
    override def contents: Array[String] = Array.fill(height)(ch.toString * width)
  }
}
// 特质作为一种富接口，包含了大量的具体方法甚至是字段，很像java中的抽象类的存在，不同的是可以多重混入
class Point(val x: Int, val y: Int)
trait Rectangular {
  def topLeft: Point
  def bottomRight: Point

  def left = topLeft.x
  def right = bottomRight.x
  def width = right - left
}
abstract class Component extends Rectangular
class Rectangle(val topLeft: Point, val bottomRight: Point) extends Rectangular

// 可堆叠和改动的特质
abstract class IntQueue {
  def put(x: Int)
  def get(): Int
}
class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]()
  override def put(x: Int): Unit = buf += x
  override def get(): Int = buf.remove(0)
}
// abstract override 只有再特质中才可以定义，表示特质必须被混入某个有具体方法的具体类中
trait Doubling extends IntQueue {
  abstract override def put(x:Int): Unit = super.put(2*x)
}
trait Incrementing extends IntQueue {
  abstract override def put(x:Int):Unit= super.put(x+1)
}
trait Filtering extends IntQueue {
  abstract override def put(x:Int):Unit= if(x>=0) super.put(x)
}
object Ch10 {
  def main(args: Array[String]): Unit = {
    // Null是任何AnyRef的子类，是null对应的类型
    val anyRefNull: Null = null
    // val num:Int=anyRefNull 报错
    // Nothing是任何类的子类，无对应的实例
    // val num: Int = ???

    // Element demo
    val ele = Element.elem(Array("hello")) above Element.elem(Array("world!"))
    println(ele)
    // rectangle
    val rect = new Rectangle(new Point(1, 1), new Point(10, 10))
    println(rect.width)

    // 特质混入的次序，越靠近右侧的特质越先调用，最后一次super调用将指向具体类的具体方法
    val queue=new BasicIntQueue with Incrementing with Filtering
    queue.put(-1);queue.put(0);queue.put(1)
    queue.get() // 1
    queue.get() // 2
  }
}
