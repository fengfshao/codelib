package com.shaoff.dig.proxy.handler;


import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimerHandler implements InvocationHandler, Serializable {

    private Object real;

    public TimerHandler(Object real) {
        this.real = real;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object res = method.invoke(real, args);
        long elapsed = System.currentTimeMillis() - start;
        log.info(method.getName()+" elapsed :"+elapsed);
        return res;
    }
}
