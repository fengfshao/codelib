package com.berry.scalaProgramming
import org.scalatest.FunSuite

/**
 * 基于ScalaTest进行单测
 *
 * @author leonardo
 * @since 2024/10/13
 *
 */
class ElementSuite extends FunSuite {
  test("elem result should have passed width") {
    val ele = Element.elem('x', 2, 3)
    assertResult(2){
      ele.width
    }
    intercept[IllegalArgumentException]{
     Element.elem('x',-2,3)
    }
  }
}
