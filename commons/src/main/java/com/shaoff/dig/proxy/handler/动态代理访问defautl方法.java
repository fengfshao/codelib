package com.shaoff.dig.proxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class 动态代理访问defautl方法 {

    static interface IService {
        public default void name() {
            System.out.println("jlljk");
        }

        public void sayHello();
    }

    static class RealService implements IService {

        @Override
        public void sayHello() {
            System.out.println("hello");
        }
    }

    /**
     * 在动态代理中，方法调用被传递到了handler里，用户根据需要重载invoke方法，
     * 在invoke方法中，如果需要调用被代理的对象的被代理方法，必然是需要真实对象的,
     *
     */
    public static void main(String[] args) {
        IService realService = new RealService();
        IService proxyService = (IService) Proxy.newProxyInstance(IService.class.getClassLoader(),
                new Class<?>[]{IService.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("entering " + method.getName());
                        System.out.println(this.getClass());
                        // todo
                        Object res = method.isDefault() ? method.invoke(this ,args):  method.invoke(realService) ;
                        System.out.println("leaving " + method.getName());
                        return res;
                    }
                });
        proxyService.name();
    }
}
