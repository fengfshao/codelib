package com.berry.spark.sql

import com.berry.spark.SparkLocal

/**
 * 验证null空值的处理问题
 *
 * @author leonardo
 * @since 2024/8/21
 *
 */
object NullJoin extends SparkLocal {
  val userTable: Seq[User] =
    Seq(User(1, "jerry", "13888332019"), User(2, "leo", "13888337019"), User(3, "tina", null))
  val orderTable: Seq[Order] =
    Seq(Order(0, "hua", 1.3d, "13888332019"), Order(1, "wolf", 3.3d, "13888337019"), Order(2, "cool", 3d, null))


  def main(args: Array[String]): Unit = {
    import spark.implicits._
    spark.sparkContext.parallelize(userTable).toDF().createOrReplaceTempView("t_user")
    spark.sparkContext.parallelize(orderTable).toDF().createOrReplaceTempView("t_order")

    /* 验证inner join时的null key,会过滤掉，不会join */
    spark.sql(
      """
        |select *
        |FROM t_user
        |join t_order
        |on t_user.phone=t_order.phone
        |limit 10
        |""".stripMargin).show(100)

    /* 验证left join时的null key,不会过滤掉，也不会根据null join*/
    spark.sql(
      """
        |select *
        |FROM t_user
        |left join t_order
        |on t_user.phone=t_order.phone
        |limit 10
        |""".stripMargin).show(100)

    // spark中涉及null join的处理
    // https://stackoverflow.com/questions/41728762/including-null-values-in-an-apache-spark-join
    println("...")
  }
}
