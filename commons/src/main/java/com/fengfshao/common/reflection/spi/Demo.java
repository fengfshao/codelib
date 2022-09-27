package com.fengfshao.common.reflection.spi;

import com.fengfshao.common.reflection.ScalarFunction;
import java.util.ServiceLoader;

/**
 * 基于ServiceLoader的方式来反射创建指定的类，原理类似通过配置传递类限定符
 * 只是这个配置有特定的格式，见META-INF/services/
 *
 * @author fengfshao
 */
public class Demo {

    public static void main(String[] args) {
        ServiceLoader<ScalarFunction> functions = ServiceLoader.load(ScalarFunction.class);
        for (ScalarFunction f : functions) {
            System.out.println(f.getClass().getSimpleName());
        }
    }
}
