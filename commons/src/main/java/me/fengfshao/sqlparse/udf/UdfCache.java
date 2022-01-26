package me.fengfshao.sqlparse.udf;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.fengfshao.common.annotations.Udf;
import org.reflections.Reflections;

/**
 * Author: fengfshao
 * Date: 2021/10/28 16:30
 *
 * Description:
 */
public class UdfCache {
    public static final Map<String,ScalarFunction> cache=new ConcurrentHashMap<>();
    static {
        Reflections reflections = new Reflections("me.fengfshao.sqlparse.udf");
        Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Udf.class);
        clazzs.forEach(c->{
            try {
                ScalarFunction f= (ScalarFunction) c.newInstance();
                cache.put(f.getName(), f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
