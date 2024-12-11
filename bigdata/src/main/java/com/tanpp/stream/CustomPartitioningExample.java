package com.tanpp.stream;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CustomPartitioningExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream<String> dataStream = env.fromElements("key1-element1", "key2-element2" );
 
        dataStream.partitionCustom(new Partitioner<String>() {

            @Override
            public int partition(String key, int numPartitions) {
                return 1;
            }
        },k-> k.split("-",2)[0]).print();
 
        env.execute("Custom Partitioning Example");
    }
 
    public static class CustomPartitioner implements Partitioner<String> {
        @Override
        public int partition(String key, int numPartitions) {
            // 这里可以根据key的值来决定如何分区
            // 例如，如果key以"key1-"开头，则数据总是去到第0个分区
            // 如果key以"key2-"开头，则数据总是去到第1个分区
            // 这是一个简单的例子，实际应用中可以根据需要进行更复杂的逻辑处理
            if (key.startsWith("key1-")) {
                return 0;
            } else if (key.startsWith("key2-")) {
                return 1;
            }
            // 默认情况下，使用平衡的分区器
            return Math.abs(key.hashCode() % numPartitions);
        }
    }
}