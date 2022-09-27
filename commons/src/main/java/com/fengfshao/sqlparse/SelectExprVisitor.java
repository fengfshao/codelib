package com.fengfshao.sqlparse;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

/**
 * Author: fengfshao
 * Date: 2021/11/1 15:50
 * Description:
 */
public class SelectExprVisitor implements SelectItemVisitor {

    final String exprStr;
    private Map<String,Object> fieldValues;
    private final Map<String, Object> newFieldValues = new HashMap<>();
    public SelectExprVisitor(String selectExpr)  {
        if(selectExpr.startsWith("select")){
            exprStr=selectExpr;
        }else{
            exprStr = "select " + selectExpr;
        }
    }

    public List<SelectItem> parseExpression() throws Exception {
        Statement statement = new CCJSqlParserManager().parse(new StringReader(exprStr));
        Select select = (Select) statement;
        return ((PlainSelect) select.getSelectBody()).getSelectItems();
    }

    public Map<String,Object> eval(Map<String, Object> fieldValues) {
        List<SelectItem> items;
        try {
            items = parseExpression();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
        this.fieldValues=fieldValues;
        items.forEach(it->{
            it.accept(this);
        });
        return newFieldValues;
    }

    @Override
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(SelectExpressionItem sxeItem) {
        BaseExprVisitor visitor= new BaseExprVisitor();
        Alias alias= Objects.requireNonNull(sxeItem.getAlias(), "must using as naming new column!");
        visitor.fieldValues=fieldValues;
        sxeItem.getExpression().accept(visitor);
        newFieldValues.put(alias.getName(), visitor.result);
    }
}
