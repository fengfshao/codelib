package com.shaoff.dig.basic;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 关于char的本质
 *
 * @author fengfshao
 */
public class UnicodeChar {
    public static void main(String[] args) {
        /*
         char只是存储码点，两字节表示，只覆盖BMP平面，即0-65535，超出的部分需要用char数组存储
         编码规则同UTF-16
         */
        char c1 = 65535;
        // char c1=65536; 编译报错，需要使用char数组表示
        char[] c2 = Character.toChars(65536);
        assert c2.length == 2;

        /* string底层只是char数组，与字节流转换时需要绑定编码 */
        String s1 = "abc";
        System.out.println(Arrays.toString(s1.getBytes()));
        // 转字节数组添加了2字节大小端标记
        System.out.println(Arrays.toString(s1.getBytes(StandardCharsets.UTF_16)));
        System.out.println(Arrays.toString(s1.getBytes(StandardCharsets.UTF_16BE)));
        System.out.println(new String(new byte[]{97,98}, StandardCharsets.UTF_8));
        System.out.println(new String(new byte[]{0,97,0,98}, StandardCharsets.UTF_16));
        // 搞错大小端转String就会出错
        System.out.println(new String(new byte[]{0,97,0,98}, StandardCharsets.UTF_16LE));

        /* 关于utf-16中的bmp平面和辅助平面*/
        char c3='\u0041';
        assert Character.isBmpCodePoint(0x0041);
        // char c4='\u1f440';
        assert Character.isSupplementaryCodePoint(0x1f440);
    }
}


