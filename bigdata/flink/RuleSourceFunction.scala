package com.homework.da.ruleengine.internal.rule

import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

import com.homework.da.ruleengine.format._
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

/**
 * Author: shaoff
 * Date: 2020/8/27 17:29
 * Package: com.homework.da.ruleengine.internal.rule
 * Description:
 *
 */
class RuleSourceFunction(ruleSource: RuleSource, props: Properties) extends RichSourceFunction[Rule] {

  private val isDone = new AtomicBoolean()

  override def run(ctx: SourceFunction.SourceContext[Rule]): Unit = {
    val refreshIntervalSec = props.getProperty("refresh.interval.sec", "60").toInt
    while (!isDone.get()) {
      ruleSource.fetchRules().foreach(ctx.collect)
      Thread.sleep(1000 * refreshIntervalSec)
    }
  }

  override def cancel(): Unit = {
    isDone.set(true)
  }
}

