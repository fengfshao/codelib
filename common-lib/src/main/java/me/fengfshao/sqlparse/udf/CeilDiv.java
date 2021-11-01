package me.fengfshao.sqlparse.udf;

import java.lang.annotation.Annotation;
import java.util.List;
import me.fengfshao.common.annotations.Udf;

/**
 * Author: fengfshao
 * Date: 2021/10/28 16:45
 * Package: me.fengfshao.sqlparse.udf
 * Description:
 */
@Udf
public class CeilDiv implements ScalarFunction {

    @Override
    public Object eval(List<Object> params) {
        String num1 = String.valueOf(params.get(0));
        String num2 = String.valueOf(params.get(1));
        return -Math.floorDiv(-Integer.parseInt(num1),Integer.parseInt(num2));
    }

    @Override
    public String getName() {
        return ScalarFunction.super.getName();
    }
}
