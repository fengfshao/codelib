package com.fengfshao.sqlparse.udf;

import com.fengfshao.common.reflection.annotations.Udf;

import java.util.List;

@Udf(name = "length")
public class Length implements ScalarFunction {

    @Override
    public Object eval(List<Object> params) {
        return ((String) params.get(0)).length();
    }

    @Override
    public String getName() {
        return ScalarFunction.super.getName();
    }
}
