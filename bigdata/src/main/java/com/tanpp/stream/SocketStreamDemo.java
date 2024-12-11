package com.tanpp.stream;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink本地调试，启动前执行 nc -lk 8888 创建监听的socket
 *
 * @author leonardo
 * @since 2024/7/10
 */

public class SocketStreamDemo {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setInteger("rest.port", 8081);
        configuration.setInteger("taskmanager.numberOfTaskSlots", 4);
        configuration.setInteger("parallelism.default", 4);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        DataStreamSource<String> lines = env.socketTextStream("localhost", 8999);
        DataStream<String> uppered = lines.map(String::toUpperCase);

        uppered.print();
        env.execute("...");
    }
}