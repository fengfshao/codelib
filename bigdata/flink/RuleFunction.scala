package com.homework.da.ruleengine.internal.rule

import com.homework.da.ruleengine.format.{Comparator, GroupedEvent}

/**
 * Author: shaoff
 * Date: 2020/8/4 16:38
 * Package: com.homework.da.ruleengine.internal
 * Description:
 *
 */


trait RuleFunction {
  def check(ge: GroupedEvent): Boolean
  def name: String
}

class MaxFunc extends RuleFunction {
  override def check(ge: GroupedEvent): Boolean = {
    val values = ge.values
    val threshHold = ge.rule.computeMethod.threshHold
    ge.rule.computeMethod.comparator match {
      case Comparator.lt =>
        values.max < threshHold
      case Comparator.gt =>
        values.max > threshHold
      case _ => false
    }
  }

  override def name: String = "max"
}

class MinFunc extends RuleFunction {
  override def check(ge: GroupedEvent): Boolean = {
    val values = ge.values
    val threshHold = ge.rule.computeMethod.threshHold
    ge.rule.computeMethod.comparator match {
      case Comparator.lt =>
        values.min < threshHold
      case Comparator.gt =>
        values.min > threshHold
      case _ => false
    }
  }
  override def name: String = "min"

}

class ForeachFunc extends RuleFunction {
  override def check(ge: GroupedEvent): Boolean = {
    val values = ge.values
    val threshHold = ge.rule.computeMethod.threshHold
    ge.rule.computeMethod.comparator match {
      case Comparator.lt =>
        values.forall(_ < threshHold)
      case Comparator.gt =>
        values.forall(_ > threshHold)
      case _ => false
    }
  }
  override def name: String = "foreach"

}

class AvgFunc extends RuleFunction {
  override def check(ge: GroupedEvent): Boolean = {
    val values = ge.values
    val threshHold = ge.rule.computeMethod.threshHold
    val avg = values.sum / values.size
    ge.rule.computeMethod.comparator match {
      case Comparator.lt =>
        avg < threshHold
      case Comparator.gt =>
        avg > threshHold
      case _ => false
    }
  }
  override def name: String = "avg"
}