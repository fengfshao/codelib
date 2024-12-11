package com.berry.spark.types

import com.berry.spark.SparkLocal
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.udf

import java.util
import scala.collection.{JavaConverters, mutable}
import scala.util.hashing.MurmurHash3

/**
 * 验证map类型的有序性
 * https://github.com/apache/hive/commit/84481cf95693f8f6c83674e0494dba30e53ae945#diff-c1f3977430f23b136163c62b4f1583d93d9d879728120a71c977d6a26f4b5cfb
 *
 * https://blog.csdn.net/weixin_43161811/article/details/135333522
 * https://spark.apache.org/docs/latest/api/sql/index.html#map_entries
 *
 * @author leonardo
 * @since 2024/8/22
 *
 */

case class StringInt64List(feaStr: String, values: List[Long])
case class Int64FloatList(feaKey: Long, values: List[Float])

object OrderMapDemo extends SparkLocal {
  def main(args: Array[String]): Unit = {
    val f1 =
      List(StringInt64List("abc", List(13)), StringInt64List("sc", List(0, 1)), StringInt64List("db", List(32, 12)))
    val f2 =
      List(
        Int64FloatList(12, List(0.28f)),
        Int64FloatList(31, List(0.1f, 0.09f)),
        Int64FloatList(10, List(3.2f, 12f)),
        Int64FloatList(14, List(3.2f, 12f)),
        Int64FloatList(8, List(3.2f, 12f)),
        Int64FloatList(19, List(3.2f, 12f)))

    val rdd = spark.sparkContext.parallelize(List(List(f1, f2)))
    val rowRdd = rdd.map { r =>
      val c1: Seq[Product] = r(0)
      val c2: Seq[Product] = r(1)

      val tmpList1 = new util.ArrayList[StringInt64List]()
      c1.foreach { ele =>
        tmpList1.add(ele.asInstanceOf[StringInt64List])
      }
      val tmpList2 = new util.ArrayList[Int64FloatList]()
      c2.foreach { ele =>
        tmpList2.add(ele.asInstanceOf[Int64FloatList])
      }

      val mapC1 = new mutable.LinkedHashMap[String, List[Long]]
      JavaConverters.asScalaBufferConverter(tmpList1).asScala.foreach { data =>
        mapC1.put(data.feaStr, data.values)
      }

      val mapC2 = new mutable.LinkedHashMap[Long, List[Float]]
      JavaConverters.asScalaBufferConverter(tmpList2).asScala.foreach { data =>
        mapC2.put(data.feaKey, data.values)
      }
      /*val mapC2= JavaConverters.asScalaBufferConverter(tmpList2).asScala.map { data =>
          (data.feaKey, data.values)
        }.toMap*/
      Row(mapC1, mapC2, Seq(mapC2))
    }

    /*

        val schema = StructType(
          List(
            StructField("f1", MapType(StringType, ArrayType(LongType, true), true), true),
            StructField("f2", MapType(LongType, ArrayType(FloatType, true), true), true)))

        val df = spark.createDataFrame(rowRdd, schema)
        df.write.mode(SaveMode.Overwrite).parquet("file:///Users/sakura1/projects/codelib/bigdata/output")
       df.printSchema()

    */

    /*    val sf1=StructField("f1", MapType(StringType, ArrayType(LongType, true), true), true)
        val sf2=StructField("f2", MapType(LongType, ArrayType(FloatType, true), true), true)
        val sf3=StructField("f3", ArrayType(MapType(LongType, ArrayType(FloatType, true), true), true))
        val schema = StructType(List(sf1,sf2,sf3))

        val df = spark.createDataFrame(rowRdd, schema)
        df.write.mode(SaveMode.Overwrite).parquet("file:///Users/sakura1/projects/codelib/bigdata/output2")*/

    val df = spark.read
      .parquet("file:///Users/sakura1/projects/codelib/bigdata/output2")


    df.show(100)
    // df.foreach(x=>println(x.getAs[Any](1)))
    spark.udf.register("hash32", udf((str: String) => MurmurHash3.stringHash(str)))
    val newDF = df.selectExpr("map_entries(f1) as f1", "map_entries(f2) as f2", "transform(f3,x->map_keys(x)) as f3")
    val newDF2 = df.selectExpr(
      "hash32(array_join(map_keys(nvl(null,map())),'')) as f1",
      "hash32(array_join(map_keys(f2),'')) as F_f2",
      "transform(nvl(f3,array()),x->array_join(map_keys(x),',')) as f3")

    newDF2.show(100)

    println(MurmurHash3.stringHash(""))
    /*
        newDF.foreach(row=>{
          val arr=row.getAs[Seq[Any]](1)
          //val ar2=row.getAs[Array[Any]](1)
          arr.foreach(x=>println(
            x.asInstanceOf[Row].getAs[Any]("key")+"->"+
              x.asInstanceOf[Row].getAs[Any]("value")
          ))
          //println("======X "+ar2)

          //println("====== "+row.getMap[Any,Any](1).toArray.mkString(","))
         // println(row.getAs[Any]("f2").getClass)
         //row.getAs[HashTrieMap[Any,Any]]("f2").map(p=>String.valueOf(p._1)).mkString
        })
    */

    /*
    df.selectExpr("cast(f2 as string)")
      .show(100)
    */
    /*    df.withColumn("f3",df.col("f2").cast(StringType))
      .show(100)*/
    /*
    df.withColumn("f3",df.col("f2")

      .cast(StringType))
    */
    //.show()

    /*    df.foreach(row=>{
      println(row.getStruct(1))
     println(row.getMap[Any,Any](1).toArray.mkString(","))
     // println(row.getAs[Any]("f2").getClass)
     //row.getAs[HashTrieMap[Any,Any]]("f2").map(p=>String.valueOf(p._1)).mkString
    })
    //.foreach(x=>println(s"===== $x ======"))
    df.show(10, false)*/
    /*
    df.createOrReplaceTempView("t_data")
    spark.sql("CREATE TEMPORARY FUNCTION testMyFun AS 'com.tanpp.hdfs.MyUDF'")
    */

    /*
    spark
      .sql("""
        |select testMyFun(f1,f2)
        |from t_data
        |""".stripMargin)
      .show(10)
    */
    println("...")
  }
}
