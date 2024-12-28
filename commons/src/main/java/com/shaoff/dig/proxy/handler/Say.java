package com.shaoff.dig.proxy.handler;

import java.util.function.Function;

public class Say<T> implements Function<T, Void> {
    @Override
    public Void apply(T t) {
        System.out.println(t);
        return null;
    }
}