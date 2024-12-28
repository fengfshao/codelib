package com.shaoff.dig.proxy.aopdemo;

public class ServiceA {
    @SimpleInject
    ServiceB b;
    
    public void callB(){
        b.action();
    }
}
