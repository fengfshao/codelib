package com.homework.da.ruleengine.internal.rule

import java.sql.{DriverManager, ResultSet}
import java.util.Properties

import com.homework.da.ruleengine.Logging
import com.homework.da.ruleengine.format._
import com.homework.da.ruleengine.util.JsonUtil

/**
 * Author: shaoff
 * Date: 2020/8/7 11:34
 * Package: com.homework.da.ruleengine.internal
 * Description:
 *
 */

trait RuleSource extends Serializable {
  def fetchRules(): Iterator[Rule]
}

class MySqlRuleSource(config: Properties) extends RuleSource with Logging {
  private val tableName = config.getProperty("table")
  private lazy val conn = {

    val url = config.getProperty("url")
    val u = config.getProperty("user")
    val p = config.getProperty("password")

    try { //加载driver
      Class.forName("com.mysql.cj.jdbc.Driver")
      DriverManager.getConnection(url, u, p)
    } catch {
      case e: Exception =>
        log.error(s"creating connection to $url", e)
        throw e
    }
  }

  private lazy val preparedStatement = conn.prepareStatement(s"select * from $tableName")

  private def listRules(): Iterator[Rule] = {
    val rs: ResultSet = preparedStatement.executeQuery()

    new Iterator[Rule]() {
      var preparedNext: Rule = _

      override def hasNext: Boolean = {

        while (preparedNext == null && rs.next()) {
          preparedNext = extractRule(rs).orNull
        }
        preparedNext != null
      }

      override def next(): Rule = {
        val res = preparedNext
        preparedNext = null
        res
      }
    }
  }

  private def extractRule(rs: ResultSet): Option[Rule] = {
    try {
      val ruleId = rs.getInt(1)
      val metricName = rs.getString(2)
      val projectName = rs.getString(3)
      val ruleInfoJson = rs.getString(4)
      val computeMethodJson = rs.getString(5)
      val silenceMethodJson = rs.getString(6)
      val noticeMethodJson = rs.getString(7)
      val status = rs.getInt(8)

      val r: Rule = Rule(ruleId, metricName, projectName,
        JsonUtil.toAny(ruleInfoJson, classOf[RuleInfo]),
        JsonUtil.toAny(computeMethodJson, classOf[ComputeMethod]),
        JsonUtil.toAny(silenceMethodJson, classOf[SilenceMethod]),
        JsonUtil.toAny(noticeMethodJson, classOf[NoticeMethod]), status)

      Some(r)
    } catch {
      case e: Exception =>
        log.warn("extract rule from jdbc resultset", e)
        None
    }
  }

  override def fetchRules(): Iterator[Rule] = {
    listRules()
  }
}

class MockRuleSource extends RuleSource {
  override def fetchRules(): Iterator[Rule] = {
    List(
      Rule(0, "spark_batch_delayed", "lec",
        RuleInfo("LEC Batch Congestion", AlertLevel.warning),
        ComputeMethod( Map.empty[String, String],List.empty[String], "foreach", 10, Comparator.gt, 60),
        SilenceMethod(List(HourRange(0, 6)), 3),
        NoticeMethod(NoticeType.dingding, Map("at" -> "17150012018",
          "token" -> "dec3b1a3afb0b9f48985c35f0edc8ab373948e50d9d3630382a893f6f99fff03")),
        status = 0
      ),
      Rule(1, "redis_access_duration", "lpc",
        RuleInfo("LPC Redis Hight Delay", AlertLevel.warning),
        ComputeMethod( Map.empty[String, String],List("executor"), "avg", 500, Comparator.gt, 30),
        SilenceMethod(List(HourRange(0, 6)), 2),
        NoticeMethod(NoticeType.dingding, Map("at" -> "17150012018",
          "token" -> "dec3b1a3afb0b9f48985c35f0edc8ab373948e50d9d3630382a893f6f99fff03")),
        status = 0
      )
      , Rule(2, "JedisMonitor_shaoff_StreamingMetrics_streaming_unprocessedBatches", "JedisMonitor.shaoff",
        RuleInfo("JedisMonitor 批次拥堵", AlertLevel.warning),
        ComputeMethod( Map.empty[String, String],List.empty[String], "foreach", 10, Comparator.gt, 60),
        SilenceMethod(List(HourRange(0, 6)), 3),
        NoticeMethod(NoticeType.dingding, Map("at" -> "17150012018",
          "token" -> "dec3b1a3afb0b9f48985c35f0edc8ab373948e50d9d3630382a893f6f99fff03")),
        status = 0
      ),
      //      jvm_total_used
      Rule(3, "jvm_heap_used", "TraceDataSyncHive.shaoff",
        RuleInfo("JedisMonitor 堆内存不足", AlertLevel.warning),
        ComputeMethod( Map.empty[String, String],List("executorId"), "foreach", 50692520, Comparator.gt, 180),
        SilenceMethod(List(HourRange(0, 6)), 3),
        NoticeMethod(NoticeType.dingding, Map("at" -> "17150012018",
          "token" -> "dec3b1a3afb0b9f48985c35f0edc8ab373948e50d9d3630382a893f6f99fff03")),
        status = 0
      ),
      Rule(4, "flink_taskmanager_job_task_numRecordsInPerSecond", "metric_demo_shaoff",
        RuleInfo("Source输入qps为过低", AlertLevel.warning),
        ComputeMethod(Map("task_name" -> "Source:.*"),List.empty[String], "foreach", 0.0001, Comparator.lt, 30),
        SilenceMethod(List(HourRange(0, 6)), 3),
        NoticeMethod(NoticeType.dingding, Map("at" -> "17150012018",
          "token" -> "dec3b1a3afb0b9f48985c35f0edc8ab373948e50d9d3630382a893f6f99fff03")),
        status = 0
      )
    ).iterator
  }
}
