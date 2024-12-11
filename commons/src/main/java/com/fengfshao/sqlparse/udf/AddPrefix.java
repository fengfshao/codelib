package com.fengfshao.sqlparse.udf;

import com.fengfshao.common.reflection.annotations.Udf;

import java.util.List;

@Udf(name = "add_prefix")
public class AddPrefix implements ScalarFunction {

    @Override
    public Object eval(List<Object> params) {
        return ((String) params.get(0)) +((String) params.get(1));
    }

    @Override
    public String getName() {
        return ScalarFunction.super.getName();
    }
}
