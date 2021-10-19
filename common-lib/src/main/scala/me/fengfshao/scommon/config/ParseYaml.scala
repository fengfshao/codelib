package me.fengfshao.scommon.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.yaml.snakeyaml.Yaml
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.InputStream

/**
 * Author: fengfshao
 * Date: 2021/6/25 15:10
 * Package: me.fengfshao.scommon.config
 * Description:
 *
 */
object ParseYaml {
  private val mapper: ObjectMapper = new ObjectMapper(new YAMLFactory())
  mapper.registerModule(DefaultScalaModule)

  def parse(filePath: String): Map[String, Any] = {
    val fileStream: InputStream = Thread.currentThread.getContextClassLoader
      .getResourceAsStream(filePath)
    mapper.readValue(fileStream, classOf[Map[String, Any]])
  }

  def main(args: Array[String]): Unit = {
    val s=List("aa","bb")
    val pariList= for(str<-s) yield str->s.length.toString
    val tmpMap: Map[String, String] =pariList.toMap
  }
}
