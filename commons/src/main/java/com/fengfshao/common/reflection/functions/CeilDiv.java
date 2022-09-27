package com.fengfshao.common.reflection.functions;

import com.fengfshao.common.reflection.ScalarFunction;
import com.fengfshao.common.reflection.annotations.Udf;
import java.util.List;

/**
 * 整除函数，f.eval(7,3)=2
 *
 * @author fengfshao
 */
@Udf(name = "ceil_div")
public class CeilDiv implements ScalarFunction {

    @Override
    public Object eval(List<Object> params) {
        String num1 = String.valueOf(params.get(0));
        String num2 = String.valueOf(params.get(1));
        return -Math.floorDiv(-Integer.parseInt(num1), Integer.parseInt(num2));
    }
}