package com.fengfshao.common.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * udf函数注解，每个udf实现类需添加此注解
 *
 * @author fengfshao
 */
@Target(ElementType.TYPE) //注解能用在哪里，这个Type表示只能用在类上
@Retention(RetentionPolicy.RUNTIME) //注解存在的生命周期
public @interface Udf {

    /**
     * 函数名称
     */
    String name();
}
