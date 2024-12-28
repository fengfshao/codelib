package com.berry.demo

import com.berry.demo.bean.Point

/**
 * 探索scala中的==/eq/equals/canEqual的含义
 *
 * @author fengfshao
 * @since 2023/6/29
 *
 */

object EqualsDig {
  def main(args: Array[String]): Unit = {
    val p1=new Point(1,2)
    val p2=new Point(1,2)

    // Unlike its Java counterpart, the equality operator does not compare corresponding references but turns into a null-safe equals() call for instances of AnyRef:
    println(p1==p2)
    // However, sometimes we need to check a stronger condition, like whether two references point to the same object in a heap. This is known as referential equality.
    // The operator eq and its negation counterpart ne serve this purpose in Scala:
    println(p1.eq(p2)+","+p1.ne(p2))

    //canEqual主要用于说明只有当被比较的对象是当前对象的子类或同类时才能通过。
  }
}
