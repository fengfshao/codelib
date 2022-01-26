package me.fengfshao.common.config;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Author: fengfshao
 * Date: 2021/6/25 14:53
 * Package: me.fengfshao.common.config
 * Description:
 *
 * java解析yaml配置，使用snakeyaml的类库
 */
public class ParseYaml {

    /*
     * 从Yaml中解析出Map<String,Object>
     */
    public static void parseMap() throws Exception {
        Map<String, Object> params = Collections.emptyMap();
        InputStream fileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.yaml");
        params = new Yaml().load(fileStream);
    }

    /*
     * 利用泛型方法封装类型强制转换，减小强制转换的代码面积*/
    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key, Map<String, Object> conf) {
        return (T) conf.get(key);
    }

    /*
     * 解决yaml配置中，有的值为数值，有的值为字符串数值的问题，如
     * batchSize: 100
     * batchSize: "100" */
    public static int getInt(Map<String, Object> obj, String key) {
        Object value = obj.get(key);
        if (value instanceof Number) {
            return (int) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else {
            throw new IllegalArgumentException("Cannot Cast " + value + " to Int.");
        }
    }

}
