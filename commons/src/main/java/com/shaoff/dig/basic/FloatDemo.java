package com.shaoff.dig.basic;

import java.math.BigDecimal;

/**
 * float不是完全精准的
 *
 * @author leonardo
 */

public class FloatDemo {

    public static void main(String[] args) {
        float f1 = 0.01f;
        float f3 = 0.1f;
        float f2 = f3 * 0.1f;

        BigDecimal d1 = BigDecimal.valueOf(0.1);
        BigDecimal d2 = BigDecimal.valueOf(0.1);
        System.out.println(f2);
        System.out.println(d1.multiply(d2).floatValue());
    }

}
