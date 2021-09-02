package com.homework.da.ruleengine.job

import java.util.Properties

import com.homework.da.ruleengine.Logging
import com.homework.da.ruleengine.constant.PropKeys
import com.homework.da.ruleengine.format._
import com.homework.da.ruleengine.internal._
import com.homework.da.ruleengine.internal.rule.{MockRuleSource, MySqlRuleSource, RuleFunctionPooledFactory, RuleSourceFunction}
import com.homework.da.ruleengine.util.{JsonUtil, Utils}
import org.apache.flink.api.common.functions.{AggregateFunction, RichFilterFunction, RichMapFunction}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.api.common.typeinfo.{BasicTypeInfo, TypeHint, TypeInformation}
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.util.Collector

import scala.collection.mutable
import scala.util.Try

/**
 * Author: shaoff
 * Date: 2020/7/31 11:01
 * Package: com.homework.da.job
 * Description:
 *
 * 从app.properties读取配置
 */

object MetricStreamChecking extends Logging {

  def main(args: Array[String]) {
    // Checking input parameters
    val applicationPropertiesStream = Thread.currentThread().getContextClassLoader.getResourceAsStream("app.properties")
    val params: ParameterTool = ParameterTool.fromPropertiesFile(applicationPropertiesStream)

    val props = params.getProperties

    /*val configuration=new Configuration()
    import scala.collection.JavaConverters._
    props.entrySet().asScala.foreach{p=>
      configuration.setString(p.getKey.toString, p.getValue.toString)
    }
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)*/

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.getConfig.setGlobalJobParameters(params)

    val kafkaProps: Properties = Utils.subProperties(PropKeys.SOURCE_KAFKA_PREFIX, props)
    log.info(s"kafka source properties $kafkaProps")

    val kafka: FlinkKafkaConsumer010[String] = new FlinkKafkaConsumer010[String](kafkaProps.getProperty("topic"), new SimpleStringSchema(), kafkaProps)

    val metricStream: DataStream[MetricEvent] = env.addSource[String](kafka).map(JsonUtil.toAny(_, classOf[MetricEvent]))

    val ruleMysqlProps = Utils.subProperties(PropKeys.RULE_SOURCE_MYSQL_PREFIX, props)
    val ruleSource = new MySqlRuleSource(ruleMysqlProps)

    //mock source
    //    val testRuleSource=new MockRuleSource()
    val ruleSourceFunctionProps = Utils.subProperties(PropKeys.RULE_SOURCE_FUNCTION_PREFIX, props)
    val ruleStream = env.addSource[Rule](new RuleSourceFunction(ruleSource, ruleSourceFunctionProps))
      .setParallelism(1)

    val ruleStateDescriptor: MapStateDescriptor[String, Map[Int, Rule]] = new MapStateDescriptor[String, Map[Int, Rule]]("rules",
      BasicTypeInfo.STRING_TYPE_INFO,
      TypeInformation.of(new TypeHint[Map[Int, Rule]]() {}))

    val ruleBroadcastStream: BroadcastStream[Rule] = ruleStream.broadcast(ruleStateDescriptor)

    //连接规则并过滤未加规则的metrics
    val connectedStream: DataStream[(MetricEvent, Rule)] = metricStream.connect(ruleBroadcastStream)
      .process(new BroadcastProcessFunction[MetricEvent, Rule, (MetricEvent, Rule)] {

        override def processElement(value: MetricEvent, ctx: BroadcastProcessFunction[MetricEvent, Rule, (MetricEvent, Rule)]#ReadOnlyContext, out: Collector[(MetricEvent, Rule)]): Unit = {
          val b: ReadOnlyBroadcastState[String, Map[Int, Rule]] = ctx.getBroadcastState(ruleStateDescriptor)
          //查看对应metric_name的规则
          val rules = b.get(value.name)
          if (rules != null) {
            rules.values.filter(matchRule(value, _)).foreach {
              r => out.collect((value, r))
            }
          }
        }

        override def processBroadcastElement(rule: Rule, ctx: BroadcastProcessFunction[MetricEvent, Rule, (MetricEvent, Rule)]#Context, out: Collector[(MetricEvent, Rule)]): Unit = {
          val b: BroadcastState[String, Map[Int, Rule]] = ctx.getBroadcastState(ruleStateDescriptor)
          val rules = b.get(rule.metricName)
          if (rules == null) {
            if (rule.status == 0) {
              b.put(rule.metricName, Map(rule.ruleId -> rule))
            }
          } else {
            val newRules = if (rule.status == 0) {
              Map(rule.ruleId -> rule) ++ rules
            } else {
              rules - rule.ruleId
            }
            b.put(rule.metricName, newRules)
          }
        }
      }).rebalance.assignTimestampsAndWatermarks(new BoundedOutOfOrderlessGenerator())
    //    connectedStream.print()

    val windowLengths = props.getProperty(PropKeys.COMPUTE_WINDOW_LENGTHS_SEC).split(",").map(_.toInt)
    val slidingInterval = props.getProperty(PropKeys.COMPUTE_WINDOW_INTERVAL_SEC).toInt
    assert(windowLengths.length > 0)

    var windowedStream = windowAggregation(connectedStream, windowLengths.head, slidingInterval)
    windowLengths.tail.foreach { windowLength =>
      windowedStream = windowedStream.union(windowAggregation(connectedStream, windowLength, slidingInterval))
    }

    val abnormalGroupedStream: DataStream[GroupedEvent] = windowedStream
      .filter(new RichFilterFunction[GroupedEvent] {

        var factory: RuleFunctionPooledFactory = _

        override def open(parameters: Configuration): Unit = {
          factory = new RuleFunctionPooledFactory(props)
        }

        override def filter(value: GroupedEvent): Boolean = {
          val r = factory.getRule(value.rule.computeMethod.aggType)
          if (r.isDefined) {
            r.get.check(value)
          } else {
            false

          }
        }
      })

    //根据静默规则过滤
    val silencedStream: DataStream[GroupedEvent] = abnormalGroupedStream.filter(new RichFilterFunction[GroupedEvent] {
      var lastAlertTimestamp: mutable.HashMap[String, Int] = _
      var hourAlertCount: mutable.HashMap[String, Int] = _

      override def open(parameters: Configuration): Unit = {
        lastAlertTimestamp = new mutable.HashMap[String, Int]()
        hourAlertCount = new mutable.HashMap[String, Int]()
      }

      override def filter(value: GroupedEvent): Boolean = {
        val k = value.metricName + "::" + value.projectName
        val lastTimestampSec = lastAlertTimestamp.getOrElse(k, -1)
        val nowTimestampSec: Int = (System.currentTimeMillis() / 1000).toInt

        //每分钟最多一次
        if (nowTimestampSec - lastTimestampSec < 60) return false
        //每小时最多n次
        val n = value.rule.silenceMethod.maxTimesPerHour
        if (lastTimestampSec == -1 || nowTimestampSec - lastTimestampSec > 3600) {
          lastAlertTimestamp.put(k, nowTimestampSec)
          hourAlertCount.put(k, 1)
          true
        } else {
          val currentCount = hourAlertCount(k)
          if (currentCount < n) {
            lastAlertTimestamp.put(k, nowTimestampSec)
            hourAlertCount(k) = currentCount + 1
            true
          } else {
            false
          }
        }
      }
    })

    val alertProps = Utils.subProperties(PropKeys.ALERT_PREFIX, props)
    silencedStream.map(AlertRequest(_)).map(new RichMapFunction[AlertRequest, Unit] {
      var alertAsyncSender: AlertAsyncSender = _

      override def open(parameters: Configuration): Unit = {
        alertAsyncSender = new AlertAsyncSender(alertProps)
      }

      override def map(value: AlertRequest): Unit = {
        log.debug(value.toString)
        alertAsyncSender.send(value)
      }
    })

    env.execute("MetricStream Checking")
  }

  def windowAggregation(preparedStream: DataStream[(MetricEvent, Rule)], windowSizeSec: Int, intervalSec: Int): DataStream[GroupedEvent] = {
    val keySelector = (p: (MetricEvent, Rule)) => {
      //某些指标可能还会按照一些tag进行聚合，如redis_delay{executor="host1"} a
      //此时key=redis_delay_host1
      val m = p._1
      val c = p._2.computeMethod
      val tagsValue = Try {
        c.groupTags.map(tag => m.tags(tag)).mkString("_")
      }.getOrElse("")
      m.name + "_" + m.tags("project") + "_" + tagsValue
    }

    val windowedStream: DataStream[GroupedEvent] = preparedStream.filter(_._2.computeMethod.windowSize == windowSizeSec)
      .keyBy(keySelector)
      .window(SlidingEventTimeWindows.of(Time.seconds(windowSizeSec), Time.seconds(intervalSec)))
      .aggregate(new AggregateFunction[(MetricEvent, Rule), Option[GroupedEvent], GroupedEvent] {
        override def createAccumulator(): Option[GroupedEvent] = None

        override def add(p: (MetricEvent, Rule), accumulator: Option[GroupedEvent]): Option[GroupedEvent] = {
          val m = p._1
          val c = p._2.computeMethod
          if (accumulator.isEmpty) {
            val groupTagsMap = m.tags.filterKeys(c.groupTags.contains(_))
            Some(GroupedEvent(m.name, m.tags("project"), groupTagsMap, List(m.value), p._2))
          } else {
            val newValues = m.value :: accumulator.get.values
            accumulator.map(_.copy(values = newValues))
          }
        }

        override def getResult(accumulator: Option[GroupedEvent]): GroupedEvent = {
          assert(accumulator.nonEmpty)
          accumulator.get
        }

        override def merge(a: Option[GroupedEvent], b: Option[GroupedEvent]): Option[GroupedEvent] = {
          val newValues = a.get.values ::: b.get.values
          Some(a.get.copy(values = newValues))
        }
      })
    windowedStream
  }

  def matchRule(m: MetricEvent, r: Rule): Boolean = {
    val metricMatch = m.name.equals(r.metricName)
    if (!metricMatch) return false
    val projectMatch = m.tags.get("project").map(_.matches(r.projectName))
    if (!projectMatch.getOrElse(false)) return false
    val tagsMatch = r.computeMethod.filterTags.forall(p => {
      val v = m.tags.get(p._1)
      v.nonEmpty && v.get.matches(p._2)
    })
    if (!tagsMatch) return false
    true
  }
}