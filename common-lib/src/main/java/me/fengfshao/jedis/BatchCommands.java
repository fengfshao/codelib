package me.fengfshao.jedis;

import java.util.List;
import java.util.Map;

/**
 * Author: shaoff
 * Date: 2021/9/2 00:48
 * Package: me.fengfshao.jedis
 * Description:
 */
public interface BatchCommands {
    Map<String, String> batchHGet(List<String> keys, String field);
    void batchHSet(List<String> keys, String field);
}
