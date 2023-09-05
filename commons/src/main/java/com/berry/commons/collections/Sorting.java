package com.berry.commons.collections;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 验证排序的问题
 *
 * @author fengfshao
 * @since 2023/7/19
 */

public class Sorting {

    public static void main(String[] args) {
        List<Entry<String, Long>> testData = new ArrayList<>();
        testData.add(new SimpleEntry<>("aaa", 10L));
        testData.add(new SimpleEntry<>("bbb", 22L));
        testData.add(new SimpleEntry<>("ccc", 31L));

        testData.sort((o1, o2) -> {
            if (o2.getValue() < o1.getValue()) {
                return -1;
            } else if (o1.getValue() == o2.getValue()) {
                return 0;
            } else {
                return 1;
            }
        });

        System.out.println(testData.subList(0,2));
    }
}
