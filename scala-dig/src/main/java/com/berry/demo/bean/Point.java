package com.berry.demo.bean;

import java.util.Objects;

/**
 * 普通实体，有X,Y两个字段，用于验证equals
 *
 * @author fengfshao
 * @since 2023/6/29
 */

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * 一个完全错误的 equals 定义，只是重载了equals方法而非override
     * 当执行point.equals(Object)时会失败
     */
/*    public boolean equals(Point other) {
        return (this.getX() == other.getX() && this.getY() == other.getY());
    }*/
    @Override
    public boolean equals(Object o) {
        System.out.println("equals called");
        if (this == o) {
            return true;
        }
        if (o != null && o instanceof Point) {
            Point point = (Point) o;
            return ((Point) o).canEqual(this) && getX() == point.getX() && getY() == point.getY();
        }
        return false;
    }

    public boolean canEqual(Object o) {
        return o instanceof Point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
