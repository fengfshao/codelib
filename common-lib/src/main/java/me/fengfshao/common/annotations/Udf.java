package me.fengfshao.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: fengfshao
 * Date: 2021/7/19 16:39
 * Package: com.tencent.pcg.annotations
 * Description:
 */
@Target(ElementType.TYPE) //注解能用在哪里，这个Type表示只能用在类上
@Retention(RetentionPolicy.RUNTIME) //注解存在的生命周期
public @interface Udf {

    public enum Types {StringType, IntType, LongType, DoubleType, BooleanType, ArrayType}
    public String name() default "";
    public Types returnType() default Types.StringType;
}


/* 用法示例
@Udf
class  Demo{

}
*/
