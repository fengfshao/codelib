package com.shaoff.dig.basic.bean;

import java.util.Objects;

/**
 * 继承自Point，观察equals在继承机制下的表现
 *
 * @author fengfshao
 * @since 2023/6/29
 */

public class ColoredPoint extends Point {

    private final Color color;

    public ColoredPoint(int x, int y,Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ColoredPoint that = (ColoredPoint) o;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color);
    }

    @Override
    public boolean canEqual(Object o) {
        return o instanceof ColoredPoint;
    }
}
