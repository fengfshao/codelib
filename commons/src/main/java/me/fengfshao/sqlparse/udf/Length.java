package me.fengfshao.sqlparse.udf;

import java.util.List;
import me.fengfshao.common.annotations.Udf;

/**
 * Author: fengfshao
 * Date: 2021/10/28 16:24
 * Package: me.fengfshao.sqlparse.udf
 * Description:
 */
@Udf
public class Length implements ScalarFunction{

    @Override
    public Object eval(List<Object> params) {
        String value= (String) params.get(0);
        return value.length();
    }
}
