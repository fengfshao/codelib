package com.fengfshao.common.async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 异步CompletableFuture的使用
 *
 * @author fengfshao
 * @since 2023/7/21
 */

public class CompletableFutureDemo {

    public static void main(String[] args) {
        int bound=100;
        boolean b = CompletableFuture.supplyAsync(() -> {
            // 模拟请求外部数据
            Random rand = ThreadLocalRandom.current();
            try {
                Thread.sleep(rand.nextInt(10));
            } catch (InterruptedException ignored) {
            }
            return rand.nextInt(bound);
        }).thenCompose(a -> {
            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }
            return CompletableFuture.supplyAsync(() -> a > 50);
        }).join();

        System.out.println(b);
    }
}
