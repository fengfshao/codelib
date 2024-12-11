package com.tanpp.stream.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * 延迟处理
 *
 * @author leonardo
 * @since 2024/9/3
 */
@Slf4j
public class DelayProcess extends KeyedProcessFunction<String, String, String> {

    private final long delayGap;
    private ValueState<String> state;


    public DelayProcess(long delayGap) {
        this.delayGap = delayGap;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        state = getRuntimeContext().getState(new ValueStateDescriptor<>("myState", String.class));
    }

    @Override
    public void onTimer(long timestamp, KeyedProcessFunction<String, String, String>.OnTimerContext ctx,
            Collector<String> out) throws Exception {
        String r = state.value();
        out.collect(state.value());
/*
        if (ctx.getCurrentKey().equals(r)) {
            out.collect(state.value());
        }
*/
    }

    @Override
    public void processElement(String value, KeyedProcessFunction<String, String, String>.Context ctx,
            Collector<String> collector) throws Exception {
        state.update(value);
        long ts = (System.currentTimeMillis() / 3000) * 3000 + delayGap;
        ctx.timerService().registerProcessingTimeTimer(ts);
        log.warn("process {} when {} of {}", value, LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()),ts);

    }
}
