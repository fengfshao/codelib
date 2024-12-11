package com.fengfshao.common.constants;

import java.util.regex.Pattern;

/**
 * 常见的字符串匹配模式
 *
 * @author fengfshao
 * @since 2023/7/26
 */

public class Patterns {

    /**
     * 邮编，6位数字
     */
    public static Pattern ZIP_CODE = Pattern.compile(
            "(?<![0-9])" // 左边无数字，否定逆序环视
                    + "[0-9]{6}"
                    + "(?![0-9])"); // 右边无数字，否定顺序环视

    /**
     * 手机号码，目前手机号第1位都是1，第2位取值为3、4、5、7、8之一
     */
    public static Pattern MOBILE_PHONE_NUMBER = Pattern.compile("1[34578][0-9]{9}");

    /**
     * 固定电话，区号和市内号码，区号是3到4位，市内号码是7到8位。区号以0开头
     */
    public static Pattern PHONE_NUMBER = Pattern.compile("0[0-9]{2,3}[0-9]{7,8}");

    /**
     * 日期，匹配2016-01-11这类格式
     */
    public static Pattern DATE = Pattern.compile("\\d{4}(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[01])");

    /**
     * 时间，配置23:01这类格式
     */
    public static Pattern TIME = Pattern.compile("\\d{2}:\\d{2}");

    /**
     * 身份证号
     */
    public static Pattern ID_NUMBER = Pattern.compile("\\d{10}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[01])[0-9xX]{4}");


    public static void main(String[] args) {
        System.out.println(ID_NUMBER.matcher("41272119722302583X").matches());
    }
}
