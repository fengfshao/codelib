package com.shaoff.dig.basic.box;

import java.util.Map;

/**
 * 验证一下自动拆装遇到null时的情况
 *
 * @author fengfshao
 * @since 2023/7/18
 */

public class CaseNull {

    public static void main(String[] args) {
        Map<String, Boolean> m = new java.util.HashMap<String, Boolean>();
        m.put("aaa", true);

        Boolean b = m.get("bbb");
        if (b != null) {
            System.out.println(b.booleanValue());
        }
        // 这里果断会空指针异常
        boolean c = m.get("bbb");
        System.out.println(c);
    }
}
