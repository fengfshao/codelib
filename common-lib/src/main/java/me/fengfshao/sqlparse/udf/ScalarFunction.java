package me.fengfshao.sqlparse.udf;

import com.google.common.base.CaseFormat;
import java.util.List;

/**
 * Author: fengfshao
 * Date: 2021/10/28 16:19
 * Package: me.fengfshao.sqlparse.udf
 * Description:
 */
public interface ScalarFunction {
    Object eval(List<Object> params);
    default String getName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.getClass().getSimpleName());
    }
}
