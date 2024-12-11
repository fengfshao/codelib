package com.fengfshao.sqlparse.udf;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.fengfshao.common.reflection.annotations.Udf;
import org.reflections.Reflections;

/**
 *
 * @author fengfshao
 */
public class UdfCache {
    public static final Map<String,ScalarFunction> cache=new ConcurrentHashMap<>();
    static {
        Reflections reflections = new Reflections("com.fengfshao.sqlparse.udf");
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
