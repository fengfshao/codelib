package com.tanpp.stream.functions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanpp.stream.Metrics;
import com.tanpp.stream.Transaction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Slf4j
@SuppressWarnings("unchecked")
public class JsonParserFlatMap<T> extends RichFlatMapFunction<String, T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger;
    private final String metricName;
    private final Class<?> clazz;
    private final Random random = new Random();
    private int sampleBase = 1;

    public JsonParserFlatMap(String metricName, Class<?> clazz) {
        this.metricName = metricName;
        this.clazz = clazz;
        this.logger = LoggerFactory.getLogger(metricName);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // register callback for sampleBase
    }

    @Override
    public void flatMap(String value, Collector<T> out) throws Exception {
        val trans = Metrics.newTransaction(JsonParserFlatMap.class.getSimpleName(), metricName);
        try {
            T row = (T) mapper.readValue(value, clazz);
            if (random.nextInt(sampleBase) < 1) {
                logger.info("{} rawInput {}, parsed {}", metricName, value, row);
            }
            trans.setStatus(Transaction.SUCCESS);
            out.collect(row);
        } catch (Exception e) {
            logger.error("unable parse {} ", value, e);
            trans.setStatus(e);
        } finally {
            trans.complete();
        }
    }
}
