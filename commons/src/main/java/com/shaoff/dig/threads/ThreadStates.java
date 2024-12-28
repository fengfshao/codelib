package com.shaoff.dig.threads;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 线程共有runnable,running,blocked,waiting,等几种状态
 * 将一个线程从running的状态改变的方法
 * yield,告诉线程调度器当前可以切换，调度器可以无视，或是检查当钱其他优先级大于等于该线程的线程，然后执行权交给其他线程，
 * 但是此线程仍是runnable
 *
 * sleep，当前线程停止执行一段时间，受制于系统的定时器和调度器，如果没有其他线程要执行，则cpu进入空闲
 *
 * join,等待另一线程执行完毕，自己会进入WAITING
 */
public class ThreadStates {
    private static int nth=3;
    private static LinkedBlockingDeque<String> que=new LinkedBlockingDeque<>();
    private static Executor executor= Executors.newFixedThreadPool(nth);
    public static void main(String[] args) throws Exception {
        stateDemo(args);
        /*for(int i=0;i<nth;i++){
            int finalI = i;
            executor.execute(() -> {
                while (true) {
                    try {
                        String data = que.take();
                        System.out.println(finalI +":"+data);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNextLine()){
            que.push(scanner.nextLine());
        }*/
    }


    /**
     * Java线程的状态: NEW RUNNABLE BLOCKED WAITING TIMED_WAITING TERMINATED
     * 线程进入synchronized块时等待对象，进入BLOCKED
     * 执行wait,join,park进入WAITING状态
     * 执行sleep,wait with timeout,join with timeout,parkNanos，parkUntil等操作会进入，sleep不会释放监视器
     * 进入TERMINATED，由于正常完成或发生致命错误，unhandled exception
     */
    public static void stateDemo(String[] args) throws InterruptedException {
        Thread t1=new Thread(new Job1());
        Thread t2=new Thread(new Job2());
        System.out.println(t1.getName()+" "+t1.getState());
        t1.start();
        System.out.println(t1.getName()+" "+t1.getState());
        t2.start();
        System.out.println(t2.getName()+" "+t2.getState());
        System.out.println(t1.getName()+" "+t1.getState());
        Thread.sleep(50);
//        t2.start();
        System.out.println(t2.getName()+" "+t2.getState());
        Thread.sleep(1000);
        System.out.println(t2.getName()+" "+t2.getState());

    }
}


class Job1 implements Runnable{
    @Override
    public void run() {

        synchronized (ThreadStates.class) {
            try {
                Thread.sleep(100);
                ThreadStates.class.wait(1000 * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Job2 implements Runnable{
    @Override
    public void run(){
        synchronized (ThreadStates.class){
            System.out.println(Thread.currentThread().getName()+" "+"get monitor");
            Scanner scanner=new Scanner(System.in);
            System.out.println(scanner.nextInt());
        }
    }
}