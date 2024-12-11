package com.shaoff.dig.basic;

/**
 * Author: shaoff
 * Date: 2020/5/15 17:59
 * Package: serialization.org.apache.spark.serialization1.closure1.innerclass1
 * Description:
 */
public class Outer3 {

    int a1;

    void function() {
        int used = 0;
        int notUsed = -1;

        class Inner3 {

            void f2() {
                int t1 = used;
                int t2 = a1;
            }
        }
    }
}