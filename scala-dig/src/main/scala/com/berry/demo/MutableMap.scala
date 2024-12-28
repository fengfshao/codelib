package com.berry.demo

import scala.collection.mutable

/**
 * scala中map使用
 *
 * @author fengfshao
 * @since 2023/6/20
 *
 */
object MutableMap {

  def main(args: Array[String]): Unit = {
    val scores = mutable.Map("Alice" -> 10, "Bob" -> 3)
    // add
    scores("Cindy") = 8
    scores += ("Fred" -> 3, "Bob" -> 6)
    // update
    scores("Alice") = 11
    // delete
    scores -= "Fred"
    // set default value
    println(scores.withDefault(_.length)("Lee"))
    println(scores.withDefaultValue(0)("Lee"))
  }
}
