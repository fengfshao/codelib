package com.shaoff.dig.serialization.ma;

import java.io.Serializable;

/**
 * Author: shaoff
 * Date: 2020/5/2 17:18
 * Package: serialization.ma
 * Description:
 */
class Common implements Serializable {
    String c;

    public Common(String c) {
        this.c = c;
    }
}

class A implements Serializable {
    String a;
    Common common;

    public A(String a, Common common) {
        this.a = a;
        this.common = common;
    }

    public Common getCommon() {
        return common;
    }
}

class B implements Serializable {
    String b;
    Common common;

    public B(String b, Common common) {
        this.b = b;
        this.common = common;
    }

    public Common getCommon() {
        return common;
    }
}