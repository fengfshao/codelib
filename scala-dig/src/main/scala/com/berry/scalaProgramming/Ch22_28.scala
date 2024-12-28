package com.berry.scalaProgramming

import java.awt.Color
import scala.collection.immutable.HashSet
/**
 * scala编程第三版22-28章本地调试
 *
 * @author leonardo
 * @since 2024/10/23
 *
 */
case class Person(name: String, isMale: Boolean, children: Person*)
object Email {
  def apply(user: String, domain: String): String = user + "@" + domain
  def unapply(str: String): Option[(String, String)] = {
    val parts = str split ("@")
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }

  // TODO 抽取器的序列模式
  def unapplySeq(str: String): Option[Seq[Option[(String, String)]]] = {
    val parts = str.split(",")
    if (parts.isEmpty) None else Some(parts.map(unapply).toSeq)
  }
}
object Ch22_28 {
  def main(args: Array[String]): Unit = {
    val persons = List.empty[Person]

    // 高级for表达式，相比于高阶函数调用更有可读性
    // 每个for表达式都可以转译为map，flatMap和filter的组合
    val info = for (p <- persons; if !p.isMale; c <- p.children) yield (p.name, c.name)
    // 实际上代码会编译为
    val info2 = persons.filter(p => !p.isMale).flatMap(p => p.children.map(c => (p.name, c.name)))

    // 抽取器，模式匹配中自动调用unapply，如果返回None则匹配失败
    val emailStr: String = Email("John", "epfl.ch")
    val Email(user, domain) = "a@b"
    val ops: Option[(String, String)] = Email.unapply("John@epfl.ch")
    "John@epfl.ch" match {
      case Email(u, d) =>
    }
    // 利用正则进行抽取
    val Decimal = "(-)?(\\d+)(\\.\\d*)?".r
    val Decimal(s, i, d) = "-1.23"
    val Decimal(s1, i1, d1) = "2"
    println(s, i, d)
    println(s1, i1, d1) // s1,d1 is null

    // 对象相等性比较
    // 1.未成功重写equals
    class Point(var x: Int, val y: Int) {
      //def equals(obj: Point): Boolean = x==obj.x && y==obj.y
      override def equals(obj: Any): Boolean = obj match {
        case that: Point => x == that.x && y == that.y
        case _ => false
      }
    }
    // 2. 未定义hashcode
    val p1 = new Point(1, 2)
    val p2 = new Point(1, 2)
    assert(p1.equals(p2)) // 被重载了
    println(Set(p1).contains(p2))

    // 3. 使用可变字段
    // 未能复现，但字段变化后，的确可能使得在查找
    val coll = HashSet(p1)
    println(coll contains p1)
    p1.x += 1
    println(coll contains p1)

    // 4. 涉及继承
    class ColoredPoint(x: Int, y: Int, val color: Color) extends Point(x, y) {
      override def equals(obj: Any): Boolean = obj match {
        case that: ColoredPoint => color == that.color && super.equals(obj)
        case _ => false
      }
    }
    val coloredPoint = new ColoredPoint(1, 2, java.awt.Color.white)
    // 不满足对称性 （自反性，对称性，传递性，一致性）
    println(p2.equals(coloredPoint))
    println(coloredPoint.equals(p2))

    // 放宽允许父类与子类比较
    class ColoredPointV2(x: Int, y: Int, val color: Color) extends Point(x, y) {
      override def equals(obj: Any): Boolean = obj match {
        case that: ColoredPointV2 => color == that.color && super.equals(obj)
        case _: Point=>super.equals(obj)
        case _ => false
      }
    }
    // 满足了对称性，却不满足传递性
    val coloredPoint2=new ColoredPointV2(1,2,java.awt.Color.white)
    val coloredPoint3=new ColoredPointV2(1,2,java.awt.Color.black)
    println(p2.equals(coloredPoint2))
    println(coloredPoint2.equals(p2))
    println(p2.equals(coloredPoint3))
    println(coloredPoint3.equals(coloredPoint2))

    // 增加canEqual函数，equals中调用that.canEqual(this)判断
    class PointFinal(val x: Int, val y: Int) {
      override def equals(obj: Any): Boolean = obj match {
        case that: PointFinal => that.canEqual(this) && x == that.x && y == that.y
        case _ => false
      }
      def canEqual(that:Any): Boolean =that.isInstanceOf[PointFinal]
    }
    class ColoredPointFinal(x: Int, y: Int, val color: Color) extends PointFinal(x, y) {
      override def equals(obj: Any): Boolean = obj match {
        case that: ColoredPointFinal => that.canEqual(this) && color == that.color && super.equals(obj)
        case _ => false
      }
      override def canEqual(that: Any): Boolean = that.isInstanceOf[ColoredPointFinal]
    }
    val p3=new PointFinal(1,2)
    val p4=new ColoredPointFinal(1,2,java.awt.Color.white)
    assert(!p3.equals(p4)) // 由于p4中的canEqual返回false
    assert(!p4.equals(p3)) // 由于p4的equals不和其他类比较

    val p5=new PointFinal(1,2){
    }
    assert(p5.equals(p3)) // 子类不重写canEqual时，父类与子类可以比较，并满足对称和传递性

    // 如果仅重写equals不重写canEqual，可能会破坏对称性或传递性
    class Point3D(x: Int,y: Int,val z:Int) extends PointFinal(x,y){
      override def equals(obj: Any): Boolean = obj match {
        case that: Point3D => that.canEqual(this) && z == that.z && super.equals(obj)
        case _ => false
      }
    }
    val p6=new Point3D(1,2,3)
    assert(!p6.equals(p3))
    assert(p3.equals(p6))

    // 带泛型参数类型的Equals
    trait Tree[+T]{
      def elem:T
      def left:Tree[T]
      def right:Tree[T]
    }
    object EmptyTree extends Tree[Nothing]{
      override def elem=throw new NoSuchElementException("emptyTree")
      override def left=throw new NoSuchElementException("emptyTree")
      override def right=throw new NoSuchElementException("emptyTree")
    }
    class Branch[+T](val elem:T,val left:Tree[T],val right:Tree[T]) extends Tree[T]{
      override def equals(obj: Any): Boolean = obj match {
        case that:Branch[_]=> that.canEqual(this) && elem==that.elem && this.left==that.left && this.right==that.right
        case _=>false
      }

      def canEqual(obj:Any): Boolean =obj.isInstanceOf[Branch[_]]
      override def hashCode(): Int = {
        val state = Seq(elem, left, right)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
      }
    }
  }
}
