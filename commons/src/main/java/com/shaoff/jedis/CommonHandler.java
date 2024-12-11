package com.shaoff.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author: shaoff
 * Date: 2021/9/1 23:51
 * Package: me.fengfshao.jedis
 * Description:
 * <p>
 * retry,reconnect,monitor
 */
public class CommonHandler implements InvocationHandler {
    private final JedisPoolConfig config;
    private final String serviceName;

    public CommonHandler(String serviceName, JedisPoolConfig config) {
        this.serviceName = serviceName;
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        int currentRetry = 0;
        Jedis jedis = JedisManager.getJedis(serviceName, config);
        JedisConnectionException lastException = null;
        while (currentRetry < 3) {
            try {
                return exec(jedis, method, args);
            } catch (JedisConnectionException jce) {
                currentRetry += 1;
                lastException = jce;
                jedis = JedisManager.getJedis(serviceName, config, true);
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            } finally {
                jedis.close();
            }
        }
        System.out.println("retry 3 times failed");
        throw lastException;

    }

    private Object exec(Jedis jedis, Method method, Object[] args) throws Exception {
        /*jedis.pipelined();
        if(method.getDeclaringClass().equals(BatchCommands.class)){
            method.invoke(obj,)
        }else{
            return method.invoke(jedis, args);
        }*/
        return null;
    }


}