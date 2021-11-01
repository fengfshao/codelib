package me.fengfshao.sqlparse;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Author: fengfshao
 * Date: 2021/11/1 16:22
 * Package: me.fengfshao.sqlparse
 * Description:
 */
public class SelectExprVisitorTest {

    @Test
    public void testEval() {
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "aaa");
        row.put("ip_correlation", "{\"aaa\":123}");

        SelectExprVisitor s1 =new SelectExprVisitor("add_prefix(original_scene_status,'xyz_') as new_f1");
        SelectExprVisitor s2 =new SelectExprVisitor("length(original_scene_status) as new_f2");
        SelectExprVisitor s3 =new SelectExprVisitor("add_prefix(original_scene_status,'xyz_') as new_f1,"
                + "length(add_prefix(original_scene_status,'xyz_')) as new_f2");

        System.out.println(s1.eval(row));
        System.out.println(s2.eval(row));
        System.out.println(s3.eval(row));

    }

    /*
    @Test
    public void testSelect() {
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "aaa");
        row.put("ip_correlation", "{\"aaa\":123}");

        SelectExprVisitor s1 =getSelectVisitor("add_prefix(original_scene_status,'xyz_') as new_f1");
        SelectExprVisitor s2 =getSelectVisitor("length(original_scene_status) as new f2");

        s1.eval(row);
        s2.eval(row);

        System.out.println(row);
        //System.out.println();
    }
*/
}