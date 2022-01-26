package me.fengfshao.jedis;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.commands.JedisCommands;

import java.io.Serializable;

/**
 * Author: shaoff
 * Date: 2021/9/1 23:14
 * Package: me.fengfshao.jedis
 * Description:
 * <p>
 * jedis代理，通过代理模式实现重试、重连、监控等机制
 */
public interface JedisProxy extends JedisCommands, Serializable {
    Pipeline pipelined();
}
