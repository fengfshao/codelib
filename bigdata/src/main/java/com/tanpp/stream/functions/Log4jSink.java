package com.tanpp.stream.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * log日志 sink
 *
 * @author leonardo
 * @since 2024/8/26
 */

@Slf4j
public class Log4jSink<T> implements SinkFunction<T> {

    @Override
    public void invoke(T value, Context context) throws Exception {
        log.info("sink {} ", value);
    }
}
