package me.fengfshao.scommon.config

import com.fasterxml.jackson.databind.ObjectMapper

import scala.collection.JavaConverters.{asScalaBufferConverter, mapAsScalaMapConverter}

/**
 * Author: fengfshao
 * Date: 2021/12/12 16:15
 * Package: me.fengfshao.scommon.config
 * Description:
 *
 */
object UserHistory {
  def main(args: Array[String]): Unit = {
    val s = scala.io.Source.fromFile("/Users/sakura1/tmp/tmp2/a.log").mkString
    val m = new ObjectMapper().readValue(s, classOf[java.util.Map[String, java.util.List[String]]])
      .asInstanceOf[java.util.Map[String, java.util.List[String]]]


    //1639015200000 1638961355000L
    m.asScala.toMap.foreach { en =>
      val vid = en._1
      val ss = en._2.asScala.toList.filter(_.split(":", -1)(0).toLong > 1639152000000L)//1639152000000L
        .map(_.split(":", -1)(1)).toSet
      if (ss.nonEmpty) {
        println(vid)
        println(ss)

      }
    }
    //checkRepeatedPV2("8be13a0b8087144a342b2a081fdac393fe890010113704", m, 1638961355000L, "u33129nlqac", List("1638961355000:seq1:rep1"))
  }
}
