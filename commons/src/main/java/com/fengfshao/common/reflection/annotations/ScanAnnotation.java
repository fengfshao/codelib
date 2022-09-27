package com.fengfshao.common.reflection.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.fengfshao.common.reflection.ScalarFunction;
import org.reflections.Reflections;

/**
 * 基于注解+包扫描进行反射类注入
 *
 * @author fengfshao
 */
public class ScanAnnotation {

    private static final Map<String, ScalarFunction> functions = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Reflections reflections = new Reflections("me.fengfshao.common.reflection");
        Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Udf.class);
        for (Class<?> c : clazzs) {
            ScalarFunction function = null;
            try {
                function = (ScalarFunction) c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (function != null) {
                functions.put(c.getAnnotation(Udf.class).name(), function);
            }
        }

        System.out.println(functions);
    }
}
