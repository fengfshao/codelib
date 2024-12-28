package com.berry.demo;

import com.berry.demo.bean.Point;
import com.berry.demo.bean.Color;
import com.berry.demo.bean.ColoredPoint;
import java.util.HashSet;

/**
 * java 实现equals的几大问题
 * <a href="https://www.51cto.com/article/128507.html">Java：所有的equals方法实现都是错误的</a>
 *
 * @author fengfshao
 * @since 2023/6/29
 */

public class Equals {


    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println(p1.equals(p2));

        // 重载而非重写时输出false
        Object p2a = p2;
        System.out.println(p1.equals(p2a));

        // 没有重写hashCode时输出false
        HashSet< Point> coll = new HashSet< Point>();
        coll.add(p1);
        System.out.println(coll.contains(p2));

        // 当equals和 hashCode 取决于可变状态时，（很可能）输出 false
        p1.setX(p1.getX() + 1);
        System.out.println(coll.contains(p1));

        System.out.println(Integer.valueOf(3) instanceof Number);

        ColoredPoint cp = new ColoredPoint(1, 2, Color.RED);
        // IDE自动生成的equals过于严格，某些情况下子类和父类的对应应该视为相等
        System.out.println(cp.equals(p2));

        Point ano=new Point(1,1){
            @Override
            public int getY() {
                return 2;
            }
        };
        System.out.println(ano.equals(p2));
    }
}
