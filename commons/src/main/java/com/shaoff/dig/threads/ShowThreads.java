package com.shaoff.dig.threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ShowThreads {
    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean tmxb= ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos=tmxb.dumpAllThreads(true,true);
        for(ThreadInfo info:infos){
            System.out.println(info.getThreadId()+"--"+info.getThreadName());
        }
        Thread.sleep(1000*100);
    }

}