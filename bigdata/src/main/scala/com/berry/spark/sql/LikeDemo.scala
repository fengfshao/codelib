//package com.berry.spark.sql
//
//import com.berry.spark.SparkLocal
//
///**
// * 验证like语句
// *
// * @author leonardo
// * @since 2024/9/10
// *
// */
//
//object LikeDemo extends SparkLocal{
//  def main(args: Array[String]): Unit = {
//    val data=DataFrameData.getEmploymentCsv
//    data.show(1,0,true)
//    data.filter("Period like '2012%' or Period like '2014%'")
//      .show(5,0,true)
//  }
//}
