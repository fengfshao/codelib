package me.fengfshao.sqlparse;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: fengfshao
 * Date: 2021/10/12 20:18
 * Description:
 */
public class WhereExprVisitorTest {

    @Test
    public void evalComparator() {
        Map<String,Object> row = new HashMap<>();
        row.put("scene_status", "0");

        WhereExprVisitor w1 = new WhereExprVisitor("scene_status<>0");
        WhereExprVisitor w2 = new WhereExprVisitor("scene_status<>'0'");
        WhereExprVisitor w3 = new WhereExprVisitor("scene_status>='0'");
        WhereExprVisitor w4 = new WhereExprVisitor("scene_status<='10'");

        Assert.assertFalse(w1.eval(row));
        Assert.assertFalse(w2.eval(row));
        Assert.assertTrue(w3.eval(row));
        Assert.assertTrue(w4.eval(row));
    }

    @Test
    public void evalMath() {
        Map<String,Object> row = new HashMap<>();
        row.put("job_ts", "1000");
        row.put("scene_upload_time", "600");

        WhereExprVisitor w1 = new WhereExprVisitor("select * from a where (job_ts - scene_upload_time) / 100 > 5");
        WhereExprVisitor w2 = new WhereExprVisitor("select * from a where (job_ts - scene_upload_time) / 100 >= 4");
        WhereExprVisitor w3 = new WhereExprVisitor("select * from a where (job_ts - scene_upload_time) / 100 ='4'");

        Assert.assertFalse(w1.eval(row));
        Assert.assertTrue(w2.eval(row));
        Assert.assertTrue(w3.eval(row));
    }

    @Test
    public void evalNull() {
        Map<String,Object> row = new HashMap<>();
        row.put("job_ts", "1000");
        row.put("scene_upload_time", "600");
        row.put("bbb", "");

        WhereExprVisitor w1 = new WhereExprVisitor("select * from a where aaa*10 =0");
        WhereExprVisitor w2 = new WhereExprVisitor("select * from a where bbb is null");
        WhereExprVisitor w3 = new WhereExprVisitor("select * from a where bbb !=''");
        WhereExprVisitor w4 = new WhereExprVisitor("select * from a where aaa <>''");
        WhereExprVisitor w5 = new WhereExprVisitor("select * from a where aaa is null");

        Assert.assertTrue(w1.eval(row));
        Assert.assertFalse(w2.eval(row));
        Assert.assertFalse(w3.eval(row));
        Assert.assertTrue(w4.eval(row));
        Assert.assertTrue(w5.eval(row));
    }

    @Test
    public void testEmptyStr() {
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "");
        WhereExprVisitor w1 = new WhereExprVisitor("select * from a where original_feedid IS NOT NULL");
        WhereExprVisitor w2 = new WhereExprVisitor("select * from a where original_scene_status <> '0'");

        Assert.assertTrue(w1.eval(row));
        Assert.assertTrue(w2.eval(row));
    }

    @Test
    public void testUdf() {
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "aaa");
        row.put("ip_correlation", "{\"aaa\":123}");

        WhereExprVisitor w1 =new WhereExprVisitor("add_prefix(original_scene_status,'xyz_')='xyz_aaa'");
        WhereExprVisitor w2 =new WhereExprVisitor("length(original_scene_status)>5");
        WhereExprVisitor w3 = new WhereExprVisitor("ceil_div(13,3)=5");
        WhereExprVisitor w4 =new WhereExprVisitor("length(add_prefix(original_scene_status,'xyz_'))>5");
        WhereExprVisitor w5 =new WhereExprVisitor("get_json_object(ip_correlation,'$.aaa')=123");

        Assert.assertTrue(w1.eval(row));
        Assert.assertFalse(w2.eval(row));
        Assert.assertTrue(w3.eval(row));
        Assert.assertTrue(w4.eval(row));
        Assert.assertTrue(w5.eval(row));
    }




}