package com.fengfshao;

import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;

public class RetryUtils {

    static public void retry(CheckedRunnable runnable, int retryCount, long intervalMs)
            throws Exception {
        int count = Math.max(retryCount, 1);
        Exception ex = null;
        for (int i = 1; i <= count; i++) {
            try {
                runnable.run();
                ex = null;
                break;
            } catch (Exception e) {
                ex = e;
                Uninterruptibles.sleepUninterruptibly(intervalMs, TimeUnit.MILLISECONDS);
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

    public static <T> T retry(CheckedSupplier<T> checkedSupplier, int retryCount, long intervalMs)
            throws Exception {
        int count = Math.max(retryCount, 1);
        Exception ex = null;
        T t = null;
        for (int i = 1; i <= count; i++) {
            try {
                t = checkedSupplier.get();
                ex = null;
                break;
            } catch (Exception e) {
                ex = e;
                Uninterruptibles.sleepUninterruptibly(intervalMs, TimeUnit.MILLISECONDS);
            }
        }
        if (ex != null) {
            throw ex;
        }
        return t;
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {

        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedRunnable {

        void run() throws Exception;
    }
}
