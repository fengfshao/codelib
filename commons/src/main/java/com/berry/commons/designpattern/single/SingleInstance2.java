package com.berry.commons.designpattern.single;

/**
 * Author: tanpp
 * Date: 2021/7/21 16:52
 * Package: me.tanpp.common.designpattern.single
 * Description:
 * 静态内部类实现
 */
public class SingleInstance2 {

    private SingleInstance2() {
    }

    static private class Holder {
        static private final SingleInstance2 instance = new SingleInstance2();
    }

    static public SingleInstance2 getInstance() {
        return Holder.instance;
    }
}



