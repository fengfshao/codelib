package com.berry.demo

import com.berry.common.MathUtils
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * predef中引入了 String到 StringLike实例的隐式转换
 *
 * @author leonardo
 * @since 2024/9/12
 *
 */

object StringLikeDemo {
  def main(args: Array[String]): Unit = {
/*
    val a="123".toInt
    val buf=new ArrayBuffer[Int]()
    val res: mutable.Builder[Int, Array[Int]] =buf.mapResult(_.toArray)
    println(a)
*/

    val max =4096
    val p = 16
    val set=ArrayBuffer[Int]()
    val keys=getRebalancedKeyList(16,4096)
    println(s"Parallelism: $p,MaxParallelism: $max")
    for (i <- 0 until p) {
      val key=keys(i)
      val partition = MathUtils.murmurHash(i) % max * p / max
      println(s"i: $i,key: $key, partition: $partition")
      set.append(partition)
    }

    println(set.sorted)
  }

  /**
   * 获取重平衡后key值方法
   *
   * @param parallelism    并行度设置
   * @param maxParallelism 最大并行度设置
   * @return
   */
  def getRebalancedKeyList(parallelism: Int, maxParallelism: Int = 128): Array[Int] = {
    //println(s"Parallelism: $parallelism,MaxParallelism: $maxParallelism")
    var rebalancedKeyPartitionMap: Map[Int, Int] = Map()
    var i = 0
    while (rebalancedKeyPartitionMap.size < parallelism && i < 128) { // 当找到足够的key值或找了超过128次时，则停止查找
      val partition = keyToPartition(i, parallelism, maxParallelism)
      if (!rebalancedKeyPartitionMap.contains(partition)) {
        println((partition,i))
        rebalancedKeyPartitionMap += ((partition, i))
      }
      i += 1
    }
    rebalancedKeyPartitionMap.values.toArray
  }

  /**
   * Flink中，key到Partition转换公式
   *
   * 参考：[[KeyGroupRangeAssignment.assignKeyToParallelOperator(KeyGroupRangeAssignment#assignKeyToParallelOperator)]]
   *
   * @param key         分区key值
   * @param parallelism 设置的并行度
   * @return 分区值
   */
  def keyToPartition(key: Int, parallelism: Int, maxParallelism: Int = 128): Int = {
    MathUtils.murmurHash(key) % maxParallelism * parallelism / maxParallelism
  }

}
