package com.berry.commons.reflection;

import java.util.List;

/**
 *
 * udf算子函数接口，进行标量运算，实现类添加@Udf注解
 *
 * @author tanpp
 */
public interface ScalarFunction {

    Object eval(List<Object> params);
}
