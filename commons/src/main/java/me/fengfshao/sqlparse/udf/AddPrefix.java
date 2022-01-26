package me.fengfshao.sqlparse.udf;

import java.util.List;
import me.fengfshao.common.annotations.Udf;

/**
 * Author: fengfshao
 * Date: 2021/10/28 16:18
 * Package: me.fengfshao.sqlparse.udf
 * Description:
 */
@Udf
public class AddPrefix implements ScalarFunction {
    @Override
    public String eval(List<Object> params) {
        String colValue= (String) params.get(0);
        String prefix= (String) params.get(1);
        return prefix+colValue;
    }
}
