package com.shaoff.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: shaoff
 * Date: 2021/9/1 23:21
 * Package: me.fengfshao.jedis
 * Description:
 */
public class JedisManager {
    private static final Map<String, JedisPool> sharedPool = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private static JedisPool initPool(String serviceName, JedisPoolConfig config) {
        JedisPool pool = createJedisPool(serviceName, config);
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sharedPool.compute(serviceName, (name, pool) -> {
                    assert pool != null;
                    pool.close();
                    return createJedisPool(serviceName, config);
                });
            }
        }, 60L, 60L, TimeUnit.MINUTES);
        return pool;
    }

    private static JedisPool reInitPool(String serviceName, JedisPoolConfig config) {
        return sharedPool.compute(serviceName, (name, pool) -> {
            assert pool != null;
            pool.close();
            return createJedisPool(serviceName, config);
        });
    }

    private static JedisPool createJedisPool(String serviceName, JedisPoolConfig config) {
        return new JedisPool("", 3); // TODO
    }

    static Jedis getJedis(String serviceName, JedisPoolConfig config, boolean reInit) {
        JedisPool jedisPool;
        if (reInit) {
            jedisPool = sharedPool.compute(serviceName, (name, pool) -> {
                assert pool != null;
                pool.close();
                return createJedisPool(serviceName, config);
            });
        } else {
            jedisPool = sharedPool.computeIfAbsent(serviceName, (name -> {
                return initPool(serviceName, config);
            }));
        }
        //double check, avoid pool closed by scheduled service
        if (jedisPool.isClosed()) {
            jedisPool = sharedPool.get(serviceName);
        }
        return jedisPool.getResource();
    }

    static Jedis getJedis(String serviceName, JedisPoolConfig config) {
        return getJedis(serviceName, config, false);
    }

    public static JedisProxy getProxy(String serviceName, JedisPoolConfig config) {
        return (JedisProxy) Proxy.newProxyInstance(
                JedisProxy.class.getClassLoader(), new Class<?>[]{JedisProxy.class},
                new CommonHandler(serviceName, config));
    }
}
