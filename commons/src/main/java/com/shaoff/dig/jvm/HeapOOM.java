package com.shaoff.dig.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: shaoff
 * Date: 2020/2/27 09:30
 * Package: jvm
 * Description:
 */
public class HeapOOM {
    /*-Xms16m -Xmx32m*/
    public static void main(String[] args) throws InterruptedException {

        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory=Runtime.getRuntime().totalMemory();

        System.out.println(String.format("maxMemory: (%.2f M)", (double) maxMemory / (1024 * 1024)));
        System.out.println(String.format("totalMemory: (%.2f M)", (double) totalMemory / (1024 * 1024)));
        List<byte[]> list = new ArrayList<byte[]>();
        Thread.sleep(1000*0);

        new Thread(() -> {
//            List<byte[]> list = new ArrayList<byte[]>();
            while (true) {
                System.out.println(Thread.currentThread() + " alloc memory");
                byte[] b;
                b= new byte[1024 * 10240 * 3];

                list.add(b);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread() + " echo");
                    System.out.println(list.size());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }).start();
    }

}
