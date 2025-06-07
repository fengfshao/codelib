package com.fengfshao;

import java.util.concurrent.TimeUnit;

public class SlideTimeWindowCounter {
    private final long[] slots;
    private int slotIdx = -1;
    private int currentSum = 0;
    private final long l1;
    private final long l2;

    public SlideTimeWindowCounter(int span, TimeUnit spanUnit, int step, TimeUnit stepUnit) {
        l1 = spanUnit.toSeconds(span);
        l2 = stepUnit.toSeconds(step);
        if (l1 % l2 != 0) {
            throw new UnsupportedOperationException();
        }
        this.slots = new long[(int) (l1 / l2)];
    }


    // return newSum if current slot advanced
    public long increment(long amount, int timestamp) {
        int curSlot = getCurSlot(timestamp);
        if (curSlot == slotIdx) {
            slots[slotIdx] += amount;
            return 0;
        } else {
            if (slotIdx != -1) {
                currentSum += slots[slotIdx];
            }
            int res = currentSum;
            currentSum -= slots[curSlot];
            slots[curSlot] = amount;
            slotIdx = curSlot;
            return res;
        }
    }

    private int getCurSlot(int timestamp) {
        return (int) (timestamp % l1 / l2);
    }

    public static void main(String[] args) {
        testIncrement(args);
    }

    public static void testIncrement(String[] args) {
        SlideTimeWindowCounter counter = new SlideTimeWindowCounter(1, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);
        System.out.println(counter.increment(3, 4));
        System.out.println(counter.increment(2, 4));
        System.out.println(counter.increment(1, 7));
        System.out.println(counter.increment(7, 18));
        System.out.println(counter.increment(9, 27));
        System.out.println(counter.increment(13, 39));
        System.out.println(counter.increment(2, 40));
        System.out.println(counter.increment(5, 52)); // 37
        System.out.println(counter.increment(3, 56)); //
        System.out.println(counter.increment(2, 67));
        System.out.println(counter.increment(6, 78));
        System.out.println(counter.increment(1, 88));
    }

    public static void testGetCurSlot(String[] args) {
        SlideTimeWindowCounter counter = new SlideTimeWindowCounter(1, TimeUnit.HOURS, 5, TimeUnit.MINUTES);
        System.out.println(counter.getCurSlot(1735635360)); // 16:56:00
        System.out.println(counter.getCurSlot(1735635550)); // 16:59:10
        System.out.println(counter.getCurSlot(1735635724)); // 17:02:04
        System.out.println(counter.getCurSlot(1735637878)); // 17:37:58
        System.out.println(counter.getCurSlot(1735638513)); // 17:48:33
    }
}
