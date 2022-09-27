package com.fengfshao.sqlparse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

/**
 * Author: fengfshao
 * Date: 2021/11/1 14:51
 * Package: me.fengfshao.sqlparse
 * Description:
 */
public class TestJsql {

    public static void main(String[] args) throws Exception{
        Map<String,Object> row = new HashMap<>();
        row.put("original_feedid", "");
        row.put("original_scene_status", "aaa");
        row.put("ip_correlation", "{\"aaa\":123}");

        String exprStr="select length(original_scene_status)";


        Statement statement = new CCJSqlParserManager().parse(new StringReader(exprStr));
        Select select = (Select) statement;
        List<SelectItem> selectItems = ((PlainSelect) select.getSelectBody()).getSelectItems();

        //SelectItemVisitor visitor=new SelectItemVisitorImpl();
        selectItems.forEach(selectItem -> {
          //  selectItem.accept(visitor);
        });


    }

}
