package me.fengfshao.common.designpattern.single;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: fengfshao
 * Date: 2021/7/21 16:52
 * Package: me.fengfshao.common.designpattern.single
 * Description:
 * 双重锁机制实现
 */
public class SingleInstance1 {

    private SingleInstance1() {
    }

    static private SingleInstance1 instance;

    static public SingleInstance1 getInstance() {
        if (instance == null) {
            synchronized (SingleInstance1.class) {
                if (instance == null) {
                    instance = new SingleInstance1();
                }
            }
        }
        return instance;
    }

}



