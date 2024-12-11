package com.shaoff.jedis;

import java.util.List;
import java.util.Map;

/**
 * Author: shaoff
 * Date: 2021/9/2 00:51
 * Package: me.fengfshao.jedis
 * Description:
 */
public class BatchCommandsHandler implements BatchCommands {

    @Override
    public Map<String, String> batchHGet(List<String> keys, String field) {
        return null;
    }

    @Override
    public void batchHSet(List<String> keys, String field) {

    }
}
