package com.homework.da.ruleengine.internal.rule

import java.util.{Properties, ServiceLoader}

import com.homework.da.ruleengine.Logging

import scala.collection.mutable

/**
 * Author: shaoff
 * Date: 2020/8/27 16:02
 * Package: com.homework.da.ruleengine.internal
 * Description:
 *
 */

class RuleFunctionPooledFactory(props:Properties) extends Logging {
  private val CACHES = new mutable.HashMap[String, RuleFunction]()
  private val EXTRA_ALL_RULES_KEY = "rule.extra.impl.names"

  loadFromService()
  log.debug(s"ruleImpl from service: ${CACHES.mkString(",")}")

  private def loadAllFromProperties(): Unit = {
    val ruleClazzList: Array[RuleFunction] = System.getProperty(EXTRA_ALL_RULES_KEY, "").split(",").filter(_.nonEmpty)
      .flatMap { name => createRule(name) }

    ruleClazzList.foreach { r =>
      CACHES.put(r.name, r)
    }
  }

  private def parseRuleKey(ruleName: String): String = {
    s"rule.function.$ruleName.impl.name"
  }

  def getRule(ruleName: String): Option[RuleFunction] = {
    //CACHES中可能存在 ruleName->null，表示对应的ruleName加载过，且加载失败了
    if (!CACHES.contains(ruleName)) {
      this.synchronized {
        if (!CACHES.contains(ruleName)) {
          val clazzName = props.getProperty(parseRuleKey(ruleName))
          if (clazzName == null) {
            log.warn(s"ruleFunction impl not found,name=$ruleName")
            CACHES.put(ruleName, null)
          } else {
            val r = createRule(clazzName)
            CACHES.put(ruleName, r.orNull)
          }
        }
      }
    }
    Option(CACHES(ruleName))
  }

  private def createRule(clazzName: String): Option[RuleFunction] = {
    try {
      val c = Class.forName(clazzName)
      if (c.isAssignableFrom(classOf[RuleFunction])) {
        Some(c.newInstance().asInstanceOf[RuleFunction])
      } else {
        log.warn(s"ruleFunction impl not assignable,name=$clazzName")
        None
      }
    } catch {
      case t: Exception =>
        log.warn(s"load ruleFunction,name=$clazzName", t)
        None
    }
  }

  private def loadFromService(): Unit = {
    val serviceLoader: ServiceLoader[RuleFunction] = ServiceLoader.load(classOf[RuleFunction])
    import scala.collection.JavaConversions._
    for (func <- serviceLoader) {
      CACHES.put(func.name, func)
    }
  }
}