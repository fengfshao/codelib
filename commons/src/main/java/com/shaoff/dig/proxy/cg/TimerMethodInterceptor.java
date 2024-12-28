package com.shaoff.dig.proxy.cg;


import java.io.Serializable;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Slf4j
public class TimerMethodInterceptor implements MethodInterceptor, Serializable {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        long start = System.currentTimeMillis();
        Object res = proxy.invokeSuper(obj, args);
        long elapsed = System.currentTimeMillis() - start;
        log.info(method.getName() + " elapsed: " + elapsed);
        return res;
    }
}
