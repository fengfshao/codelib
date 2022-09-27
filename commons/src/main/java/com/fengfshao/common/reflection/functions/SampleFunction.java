package com.fengfshao.common.reflection.functions;

import com.fengfshao.common.reflection.ScalarFunction;
import java.util.List;

/**
 * ScalarFunction示例实现
 *
 * @author fengfshao
 */
public class SampleFunction implements ScalarFunction {

    @Override
    public Object eval(List<Object> params) {
        return new Object();
    }
}
