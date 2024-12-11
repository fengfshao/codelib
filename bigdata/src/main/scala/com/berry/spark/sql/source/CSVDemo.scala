package com.berry.spark.sql.source

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 读取csv格式的数据
 *
 * <a href="https://archive.apache.org/dist/spark/docs/3.3.4/sql-data-sources-csv.html">spark-sql文档</a>
 * https://www.stats.govt.nz/large-datasets/csv-files-for-download/
 *
 * @author leonardo
 * @since 2024/9/4
 *
 */
object CSVDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .master("local[4]")
      .appName("CSVDemo")
      .getOrCreate()

    val data: DataFrame = spark.read
      .option("compression", "none")
      .option("delimiter", ",")
      .option("header","true")
      .option("quote","\"")
      .csv("file:///Users/sakura1/projects/codelib/bigdata/src/main/resources/data/machine-readable-business-employment-data-mar-2024-quarter.csv")

    //data.show(10,truncae =0 ,vertical = true)

    data.filter("Period like '2012%' or Period like '2014%'")
      .show(10,0,true)
  }
}
