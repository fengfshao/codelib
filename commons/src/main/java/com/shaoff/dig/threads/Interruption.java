package com.shaoff.dig.threads;

import java.util.Scanner;

public class Interruption {

    static void IODemo2() throws InterruptedException {
        //IO阻塞时被打断自动抛异常,但需要io本身是可打断的
        //可打断的io被打断时跑出的是InterruptedIOException
        //InputStream IO可被打断的特性好像早就被jdk移除了
        Thread t = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

        });
        t.start();
        Thread.sleep(1000 * 1);
        t.interrupt();
    }

    static void sleepDemo() throws InterruptedException {
        //sleeping时被打断自动抛异常
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
        Thread.sleep(1000 * 1);
        System.out.println(t.getState());
        t.interrupt();
        System.out.println(t.isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        sleepDemo();
    }
}
