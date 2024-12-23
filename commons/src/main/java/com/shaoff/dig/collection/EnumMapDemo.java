package com.shaoff.dig.collection;

import java.util.EnumMap;
import java.util.Map;

/**
 * 验证enumMap用法
 *
 * @author leonardo
 * @since 2024/8/21
 */

public class EnumMapDemo {

    static enum AlarmPoints {
        STAIR1, STAIR2, LOBBY, OFFICE1, OFFICE2, OFFICE3,
        OFFICE4, BATHROOM, UTILITY, KITCHEN
    }

    static interface Command {
        void action();
    }

    public static void main(String[] args) {
        EnumMap<AlarmPoints, Command> em = new EnumMap<>(AlarmPoints.class);

        em.put(AlarmPoints.KITCHEN, () -> System.out.println("Kitchen fire!"));
        em.put(AlarmPoints.BATHROOM, () -> System.out.println("Bathroom alert!"));
        for (Map.Entry<AlarmPoints, Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ": ");
            e.getValue().action();
        }
        try {
            // If there's no value for a particular key:
            em.get(AlarmPoints.UTILITY).action();
        } catch (Exception e) {
            System.out.println("Expected: " + e);
        }
    }
}
