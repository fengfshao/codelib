package com.shaoff.dig.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Author: shaoff
 * Date: 2020/5/28 22:18
 * Package: concurrent.lock1
 * Description:
 *
 * 了解队列同步器实现
 */
public class Mutex implements Lock, java.io.Serializable {

    // Our internal helper class
    private static class Sync extends AbstractQueuedSynchronizer {
        // Reports whether in locked state
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // Acquires the lock if state is zero
        public boolean tryAcquire(int acquires) {
            assert acquires == 1; // Otherwise unused
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // Releases the lock by setting state to zero
        protected boolean tryRelease(int releases) {
            assert releases == 1; // Otherwise unused
            if (getState() == 0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // Provides a Condition
        Condition newCondition() {
            return new ConditionObject();
        }

        // Deserializes properly
        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }
    // The sync object does all the hard work. We just forward to it.
    private final Sync sync = new Sync();

    public void lock() {
        System.out.println(Thread.currentThread().getName()+" try lock");
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    static Mutex mutex=new Mutex();
    static ThreadPoolExecutor es= new ThreadPoolExecutor(5, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    static void setUp() {
        es.prestartAllCoreThreads();
    }

    public static void main(String[] args) {
        setUp();
        testLock();
    }

    public static void testLock() {
        Runnable t1=()->{
            Random random=new Random();
            sleep(random.nextInt(3));
            System.out.println(Thread.currentThread().getName()+" doing something");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            sleep(random.nextInt(3));
            System.err.println("finish critical section");
            mutex.unlock();
            sleep(random.nextInt(3));
            System.out.println(Thread.currentThread().getName()+" doing something");
        };
        es.execute(t1);
        es.execute(t1);
    }

    public static void testUnfair(){
        Runnable t1=()->{
            sleep(1);
            System.out.println(Thread.currentThread().getName()+" doing something begin");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            sleep(5);
            System.err.println("finish critical section");
            mutex.unlock();
            System.out.println(Thread.currentThread().getName()+" doing something end");
        };
        Runnable t2=()->{
            sleep(2);
            System.out.println(Thread.currentThread().getName()+" doing something begin");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            System.err.println("finish critical section");
            mutex.unlock();
            System.out.println(Thread.currentThread().getName()+" doing something end");
        };
        Runnable t3=()->{
            sleep(6);
            System.out.println(Thread.currentThread().getName()+" doing something begin");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            System.err.println("finish critical section");
            mutex.unlock();
            System.out.println(Thread.currentThread().getName()+" doing something end");
        };

        Runnable t4=()->{
            sleep(6);
            System.out.println(Thread.currentThread().getName()+" doing something begin");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            System.err.println("finish critical section");
            mutex.unlock();
            System.out.println(Thread.currentThread().getName()+" doing something end");
        };

        Runnable t5=()->{
            sleep(6);
            System.out.println(Thread.currentThread().getName()+" doing something begin");
            mutex.lock();
            System.err.println("in critical section");
            System.err.println(Thread.currentThread().getName()+" doing critical");
            System.err.println("finish critical section");
            mutex.unlock();
            System.out.println(Thread.currentThread().getName()+" doing something end");
        };

        es.execute(t1);
        es.execute(t2);
        es.execute(t3);
        es.execute(t4);
        es.execute(t5);
    }

    static void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}