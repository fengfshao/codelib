package com.homework.da.ruleengine.internal.rule;

import java.util.ServiceLoader;

/**
 * Author: shaoff
 * Date: 2020/8/28 15:42
 * Package: com.homework.da.ruleengine.internal.rule
 * Description:
 */
public class RulePooled {
    public static void main(String[] args) throws InterruptedException {
        ServiceLoader<RuleFunction> s= ServiceLoader.load(RuleFunction.class);
        System.out.println("...");
        for (RuleFunction r : s) {
            System.out.println(r);
        }

        Thread.sleep(1000*10000);
    }
}
