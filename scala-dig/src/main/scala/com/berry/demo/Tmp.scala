package com.berry.demo

/**
 *
 *
 * @author leonardo
 * @since 2024/9/12
 *
 */

  object Tmp {

  def main(args: Array[String]): Unit = {
    val s=Array("s1","","s2")
    val line=s.mkString("|")
    println(line)
    line.split("\\|",-1).foreach(println)

    val p=line->"ab"
    val p2=ArrowAssoc(p).->("cc")
    println(p)
    println(p2)
  }

}
