package me.fengfshao.sqlparse;

import java.io.StringReader;
import java.util.Map;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * Author: fengfshao
 * Date: 2021/11/1 15:50
 * Package: me.fengfshao.sqlparse
 * Description:
 */
public class WhereExprVisitor {

    final String exprStr;

    public WhereExprVisitor(String conditionExpr)  {
        if(conditionExpr.startsWith("select")){
            exprStr=conditionExpr;
        }else{
            exprStr = "select * from a where " + conditionExpr;
        }
    }

    public Expression parseExpression() throws Exception {
        Statement statement = new CCJSqlParserManager().parse(new StringReader(exprStr));
        Select select = (Select) statement;
        return ((PlainSelect) select.getSelectBody()).getWhere();
    }

    public boolean eval(Map<String, Object> fieldValues) {
        Expression expression;
        try {
            expression = parseExpression();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        BaseExprVisitor visitor= new BaseExprVisitor();
        visitor.fieldValues=fieldValues;
        expression.accept(visitor);
        return (boolean) visitor.result;
    }

}
