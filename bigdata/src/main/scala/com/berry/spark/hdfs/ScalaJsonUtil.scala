package com.berry.spark.hdfs

import org.json4s._
import org.json4s.jackson.JsonMethods._

import java.util

/**
 * scala 序列化工具，主要解决java序列化框架对部分scala特性类序列化时的问题
 * 使用json4s库，也是spark中依赖的json库
 *
 * @author fengfshao
 */
object ScalaJsonUtil {
  implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats

  def toAny[T: Manifest](s: String): T = parse(s).extract[T]

  def parseToJValue(s: String): JValue = parse(s)

  def toJson(o: Any): String = compact(toJValue(o))

  def toJValue(o: Any): JValue = Extraction.decompose(o)

  def main(args: Array[String]): Unit = {
    val source = """{ "some": "JSON source" }"""
    val jsonAst = toAny[Map[String, Any]](source)
    val newSource = Map("timestamp" -> System.currentTimeMillis()) ++ jsonAst
    val source2 = toJson(newSource)

    println(source2)
    case class C1(some: String, timestamp: Long)
    val obj = toAny[C1](source2)
    println(obj)
    val jarr=new util.ArrayList[String]()
    jarr.add("1")
    jarr.add("2")
    println(ScalaJsonUtil.toJson(jarr))
  }

}