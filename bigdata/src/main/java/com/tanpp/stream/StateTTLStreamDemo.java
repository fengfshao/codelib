package com.tanpp.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.state.StateTtlConfig.StateVisibility;
import org.apache.flink.api.common.state.StateTtlConfig.UpdateType;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * flink本地调试， State过期机制，MapState和ListState都会逐item进行过期
 *
 * @author leonardo
 * @since 2024/7/10
 */

public class StateTTLStreamDemo {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setInteger("rest.port", 8081);
        configuration.setInteger("taskmanager.numberOfTaskSlots", 4);
        configuration.setInteger("parallelism.default", 4);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        DataStream<String> lines = env.socketTextStream("localhost", 8888)
                .map(new MapFunction<String, String>() {
                    @Override
                    public String map(String value) throws Exception {
                        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
                                ZoneId.systemDefault());
                        System.out.println(value+" == "+dateTime);
                        return value;
                    }
                });
        DataStream<Tuple2<String, String>> resultStream = lines.keyBy(
                new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        return value.split(",")[0];
                    }
                }).process(new KeyedProcessFunction<String, String, Tuple2<String, String>>() {
            private MapState<String, Long> users;
            private ListState<String> userList;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.minutes(1))
                        .setUpdateType(UpdateType.OnCreateAndWrite)
                        .setStateVisibility(StateVisibility.NeverReturnExpired).build();
                MapStateDescriptor<String, Long> usersDesc = new MapStateDescriptor<String, Long>("users", String.class,
                        Long.class);
                ListStateDescriptor<String> userListDesc=new ListStateDescriptor<>("usersList",String.class);
                usersDesc.enableTimeToLive(ttlConfig);
                userListDesc.enableTimeToLive(ttlConfig);
                users = getRuntimeContext().getMapState(usersDesc);
                userList = getRuntimeContext().getListState(userListDesc);
            }


            @Override
            public void processElement(String value,
                    KeyedProcessFunction<String, String, Tuple2<String, String>>.Context ctx,
                    Collector<Tuple2<String, String>> out) throws Exception {
                users.put(value.split(",")[1], System.currentTimeMillis());
                userList.add(value.split(",")[1]);
                long currentCount = 0;
                long currentListCount=0;
                List<String> debugStr = new ArrayList<>();
                List<String> debugListStr = new ArrayList<>();
                for (Entry<String, Long> entry : users.entries()) {
                    currentCount += 1;
                    debugStr.add(entry.getKey());
                }
                for (String en : userList.get()) {
                    currentListCount+=1;
                    debugListStr.add(en);
                }
                //out.collect(Tuple2.of(value, currentCount + " === [" + String.join(",", debugStr) + "]"));
                out.collect(Tuple2.of(value, currentListCount + " === [" + String.join(",", debugListStr) + "]"));
            }
        });
        resultStream.print();
        env.execute("...");
    }
}