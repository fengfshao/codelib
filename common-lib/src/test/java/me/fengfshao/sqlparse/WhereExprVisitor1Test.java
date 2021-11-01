package me.fengfshao.sqlparse;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: fengfshao
 * Date: 2021/10/12 20:18
 * Package: com.tencent.pcg.sqlhelper
 * Description:
 */
public class WhereExprVisitor1Test {

    @Test
    public void evalComparator() {
        Map<String,Object> row = new HashMap<>();
        row.put("scene_status", "0");

        WhereExprVisitor1 w1 = new WhereExprVisitor1("select * from a where scene_status<>0");
        WhereExprVisitor1 w2 = new WhereExprVisitor1("select * from a where scene_status<>'0'");
        WhereExprVisitor1 w3 = new WhereExprVisitor1("select * from a where scene_status>='0'");
        WhereExprVisitor1 w4 = new WhereExprVisitor1("select * from a where scene_status<='10'");

        Assert.assertEquals(false, w1.eval(row));
        Assert.assertEquals(false, w2.eval(row));
        Assert.assertEquals(true, w3.eval(row));
        Assert.assertEquals(true, w4.eval(row));
    }

    @Test
    public void evalMath() {
        Map<String,Object> row = new HashMap<>();
        row.put("job_ts", "1000");
        row.put("scene_upload_time", "600");

        WhereExprVisitor1 w1 = new WhereExprVisitor1("select * from a where (job_ts - scene_upload_time) / 100 > 5");
        WhereExprVisitor1 w2 = new WhereExprVisitor1("select * from a where (job_ts - scene_upload_time) / 100 >= 4");
        WhereExprVisitor1 w3 = new WhereExprVisitor1("select * from a where (job_ts - scene_upload_time) / 100 ='4'");

        Assert.assertEquals(false, w1.eval(row));
        Assert.assertEquals(true, w2.eval(row));
        Assert.assertEquals(true, w3.eval(row));
    }

    @Test
    public void evalNull() {
        Map<String,Object> row = new HashMap<>();
        row.put("job_ts", "1000");
        row.put("scene_upload_time", "600");
        row.put("bbb", "");

        WhereExprVisitor1 w1 = new WhereExprVisitor1("select * from a where aaa*10 =0");
        WhereExprVisitor1 w2 = new WhereExprVisitor1("select * from a where bbb is null");
        WhereExprVisitor1 w3 = new WhereExprVisitor1("select * from a where bbb !=''");
        WhereExprVisitor1 w4 = new WhereExprVisitor1("select * from a where aaa <>''");
        WhereExprVisitor1 w5 = new WhereExprVisitor1("select * from a where aaa is null");

        Assert.assertEquals(true, w1.eval(row));
        Assert.assertEquals(false, w2.eval(row));
        Assert.assertEquals(false, w3.eval(row));
        Assert.assertEquals(true, w4.eval(row));
        Assert.assertEquals(true, w5.eval(row));
    }

    @Test
    public void testEmptyStr() {
        Map<String,Object> row = new HashMap<>();
        //row.putField("original_feedid", "");
        row.put("original_scene_status", "");

        WhereExprVisitor1 w5 = new WhereExprVisitor1("select * from a where original_feedid IS NOT NULL AND original_scene_status <> '0'");
        System.out.println(w5.eval(row));
    }

    @Test
    public void testUdf() {
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "aaa");
        row.put("ip_correlation", "{\"aaa\":123}");

        WhereExprVisitor1 w1 =getVisitor("add_prefix(original_scene_status,'xyz_')='xyz_aaa'");
        WhereExprVisitor1 w2 =getVisitor("length(original_scene_status)>5");
        WhereExprVisitor1 w3 = getVisitor("ceil_div(13,3)=5");
        WhereExprVisitor1 w4 =getVisitor("length(add_prefix(original_scene_status,'xyz_'))>5");
        WhereExprVisitor1 w5 =getVisitor("get_json_object(ip_correlation,'$.aaa')=123");

        System.out.println(w1.eval(row));
        System.out.println(w2.eval(row));
        System.out.println(w3.eval(row));
        System.out.println(w4.eval(row));
        System.out.println(w5.eval(row));
    }

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

    public static WhereExprVisitor1 getVisitor(String expr) {
        if (expr == null || expr.trim().length() == 0) {
            expr = "1 = 1";
        }
        try {
            String formatExpr = expr.trim();
            if (!formatExpr.startsWith("select")) {
                formatExpr = "select * from a where " + formatExpr;
            }
            WhereExprVisitor1 whereExprVisitor = new WhereExprVisitor1(formatExpr);
            whereExprVisitor.parseExpression();
            return whereExprVisitor;
        } catch (Exception e) {
            return null;
        }
    }

    public static SelectExprVisitor getSelectVisitor(String expr) {
        try {
            String formatExpr = expr.trim();
            if (!formatExpr.startsWith("select")) {
                formatExpr = "select " + expr  ;
            }
            return new SelectExprVisitor(formatExpr);
        } catch (Exception e) {
            return null;
        }
    }


}