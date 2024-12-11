package com.tanpp.stream;

import com.tanpp.stream.functions.DelayProcess;
import com.tanpp.stream.functions.Log4jSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 验证使用状态和timer进行延迟处理，keyBy后同key且相同时间即使注册多次，也只会触发一次
 *
 * @author leonardo
 * @since 2024/9/4
 */
@Slf4j
public class DelayPrint {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setInteger("rest.port", 8081);
        configuration.setInteger("taskmanager.numberOfTaskSlots", 4);
        configuration.setInteger("parallelism.default", 4);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        DataStreamSource<String> lines = env.socketTextStream("localhost", 8888);
        KeyedStream<String, String> keyedStream = lines.keyBy(
                new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        return value.split(",",2)[0];
                    }
                });

        keyedStream.process(new DelayProcess(15 * 1000))
                .setParallelism(1).addSink(new Log4jSink<>()).setParallelism(1);
        //lines.addSink(new Log4jSink<>());
        env.execute("delayPrint");
    }
}
