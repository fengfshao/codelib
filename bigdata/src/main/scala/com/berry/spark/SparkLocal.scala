package com.berry.spark

import org.apache.spark.sql.SparkSession

/**
 * 自带spark local模式的session
 *
 * @author leonardo
 * @since 2024/9/10
 *
 */

trait SparkLocal {
  implicit lazy val spark: SparkSession = SparkSession.builder().master("local[3]")
    .enableHiveSupport()
    .getOrCreate()
}
