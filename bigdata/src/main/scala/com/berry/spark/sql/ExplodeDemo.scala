package com.berry.spark.sql

import com.berry.spark.SparkLocal
case class SampleRow(val uid: Long, val sid: String, val gidList: Array[Long], val valueList: Array[String])

/**
 * 一行展开多行
 * https://zhuanlan.zhihu.com/p/707523462
 *
 * @author leonardo
 * @since 2024/9/11
 *
 */
object ExplodeDemo extends SparkLocal {
  def main(args: Array[String]): Unit = {

    val data = List(
      SampleRow(12345L, "kls1", Array(11, 12), Array("f11:s11:v11", "f12:v12")),
      SampleRow(12346L, "kls1", Array(21, 22), Array("f21:s21:v21", "f22:v22")))

    import spark.implicits._
    val df = spark.sparkContext.parallelize(data).toDF()
    df.show(10,truncate = false)
    println("...")
  }
}
