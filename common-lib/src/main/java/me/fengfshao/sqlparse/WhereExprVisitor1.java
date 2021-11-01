package me.fengfshao.sqlparse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.fengfshao.sqlparse.udf.UdfCache;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhereExprVisitor1 extends WExprBaseVisitor {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String className = "[" + getClass().getName() + "] ";

    private Object result;
    private final List<Object> resultLists;
    private transient Expression expression = null;
    private Map<String,Object> fieldValues;
    private final String exprStr;

    public WhereExprVisitor1(String exprStr) {
        this.exprStr = exprStr;
        this.resultLists = new LinkedList<>();
    }

    public String getExprStr() {
        return exprStr;
    }

    public void parseExpression() throws Exception {
        Statement statement = new CCJSqlParserManager().parse(new StringReader(exprStr));
        Select select = (Select) statement;
        expression = ((PlainSelect) select.getSelectBody()).getWhere();
    }

    public Object eval(Map<String,Object> fieldValues) {
        if (null == expression && !exprStr.isEmpty()) {
            try {
                parseExpression();
            } catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }
        if (null != expression) {
            this.fieldValues = fieldValues;
            expression.accept(this);
            return result;
        }

        return false;
    }

    private Boolean equal(Object leftValue, Object rightValue, boolean isNot) {
        boolean equal = false;
        if (leftValue == null || rightValue == null) {
            return isNot;
        }
        if (leftValue instanceof String) {
            equal = String.valueOf(leftValue).equals(String.valueOf(rightValue));
        } else if (leftValue instanceof Long) {
            equal = Long.parseLong(String.valueOf(leftValue)) == (Long
                    .parseLong(String.valueOf(rightValue)));
        } else if (leftValue instanceof Integer) {
            equal = Integer.parseInt(String.valueOf(leftValue)) == (Integer
                    .parseInt(String.valueOf(rightValue)));
        } else if (leftValue instanceof Double) {
            equal = Double.parseDouble(String.valueOf(leftValue)) == (Double
                    .parseDouble(String.valueOf(rightValue)));
        } else if (leftValue instanceof Float) {
            equal = Float.parseFloat(String.valueOf(leftValue)) == (Float
                    .parseFloat(String.valueOf(rightValue)));
        } else if (leftValue instanceof Boolean) {
            equal = Boolean.parseBoolean(String.valueOf(leftValue)) == Boolean
                    .parseBoolean(String.valueOf(rightValue));
        }
        if (isNot) {
            return !equal;
        }
        return equal;
    }

    @Override
    // Column name
    public void visit(Column var1) {
        String colName = var1.getFullyQualifiedName();
        try {
            result = fieldValues.get(colName);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Column not supported! ColName:" + colName);
        }
    }

    @Override
    public void visit(NullValue var1) {
        result = null;
    }

    @Override
    public void visit(DoubleValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(LongValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(DateValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(TimeValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(TimestampValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(StringValue var1) {
        this.result = var1.getValue();
    }

    @Override
    public void visit(SignedExpression var1) {
        var1.getExpression().accept(this);
        result = var1.toString();
    }

    @Override
    // ()
    public void visit(Parenthesis var1) {
        var1.getExpression().accept(this);
    }

    @Override
    // AND
    public void visit(AndExpression var1) {
        var1.getLeftExpression().accept(this);
        Boolean leftValue = (Boolean) result;
        var1.getRightExpression().accept(this);
        Boolean rightValue = (Boolean) result;
        result = leftValue && rightValue;
    }

    @Override
    // OR
    public void visit(OrExpression var1) {
        var1.getLeftExpression().accept(this);
        Boolean leftValue = (Boolean) result;
        var1.getRightExpression().accept(this);
        Boolean rightValue = (Boolean) result;
        result = leftValue || rightValue;
    }

    @Override
    // =
    public void visit(EqualsTo var1) {
        var1.getLeftExpression().accept(this);
        Object leftValue = result;
        var1.getRightExpression().accept(this);
        Object rightValue = result;
        result = this.equal(leftValue, rightValue, false);
    }

    @Override
    // <>
    // !=
    public void visit(NotEqualsTo var1) {
        var1.getLeftExpression().accept(this);
        Object leftValue = result;
        var1.getRightExpression().accept(this);
        Object rightValue = result;
        result = this.equal(leftValue, rightValue, true);
    }

    @Override
    // >
    public void visit(GreaterThan var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue > rightValue;
    }

    @Override
    // >=
    public void visit(GreaterThanEquals var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue >= rightValue;
    }

    @Override
    // <
    public void visit(MinorThan var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue < rightValue;
    }

    @Override
    // <=
    public void visit(MinorThanEquals var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue <= rightValue;
    }

    @Override
    // +
    public void visit(Addition var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue + rightValue;
    }

    @Override
    // -
    public void visit(Subtraction var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue - rightValue;
    }

    @Override
    // *
    public void visit(Multiplication var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue * rightValue;
    }

    @Override
    // /
    public void visit(Division var1) {
        var1.getLeftExpression().accept(this);
        Double leftValue = getDouble(result);
        var1.getRightExpression().accept(this);
        Double rightValue = getDouble(result);
        result = leftValue / rightValue;
    }

    @Override
    // %
    public void visit(Modulo var1) {
        var1.getLeftExpression().accept(this);
        if (result == null) {
            return;
        }
        Integer leftValue = Integer.parseInt(result.toString());
        var1.getRightExpression().accept(this);
        if (result == null) {
            return;
        }
        Integer rightValue = Integer.parseInt(result.toString());
        result = leftValue % rightValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    // IS NULL
    // IS NOT NULL
    public void visit(IsNullExpression var1) {
        var1.getLeftExpression().accept(this);
        boolean isnull;
        if (result instanceof List) {
            isnull = ((List<Object>) result).size() == 0;
        } else {
            isnull = result == null;
        }
        if (var1.isNot()) {
            result = !isnull;
        } else {
            result = isnull;
        }
    }

    @Override
    // ||
    public void visit(Concat var1) {
        var1.getLeftExpression().accept(this);
        String leftValue = result.toString();
        var1.getRightExpression().accept(this);
        String rightValue = result.toString();
        result = leftValue + rightValue;
    }

    @Override
    public void visit(ExpressionList var1) {
        List<Expression> exceptions = var1.getExpressions();
        for (Expression expression : exceptions) {
            expression.accept(this);
            resultLists.add(result);
        }
    }

    // IN
    @Override
    @SuppressWarnings("unchecked")
    public void visit(InExpression var1) {
        resultLists.clear();
        boolean in = false;
        var1.getLeftExpression().accept(this);
        Object leftValue = result;
        ItemsList rightItemsList = var1.getRightItemsList();
        rightItemsList.accept(this);
        for (Object inResult : resultLists) {
            // left value 为list类型时
            // in : 有一个值在right value中即为true
            // not in : 任何值都不在right value中则为true
            if (leftValue instanceof List) {
                for (Object subLeftValue : (List<Object>) leftValue) {
                    in = this.equal(subLeftValue, inResult, false);
                    if (in) {
                        break;
                    }
                }
            } else {
                in = this.equal(leftValue, inResult, false);
            }
            if (in) {
                break;
            }
        }
        if (var1.isNot()) {
            result = !in;
        } else {
            result = in;
        }
    }

    @Override
    public void visit(LikeExpression var1) {
        var1.getLeftExpression().accept(this);
        Object leftObj = result;
        if (leftObj == null) {
            leftObj = "";
        }
        String leftValue = leftObj.toString();
        var1.getRightExpression().accept(this);
        Object rightObj = result;
        if (rightObj == null) {
            rightObj = "";
        }
        String rightValue = rightObj.toString();
        boolean like;
        if (rightValue.startsWith("%") && rightValue.endsWith("%")) {
            like = leftValue.contains(rightValue.substring(1, rightValue.length() - 1));
        } else if (rightValue.startsWith("%")) {
            like = leftValue.endsWith(rightValue.substring(1));
        } else if (rightValue.endsWith("%")) {
            like = leftValue.startsWith(rightValue.substring(0, rightValue.length() - 1));
        } else {
            like = leftValue.equals(rightValue);
        }

        if (var1.isNot()) {
            result = !like;
        } else {
            result = like;
        }
    }

    private Double getDouble(Object obj) {
        double result = 0.0;
        if (obj != null) {
            try {
                result = Double.parseDouble(obj.toString());
            } catch (Exception e) {
                result = 0.0;
            }
        }
        return result;
    }

    @Override
    public void visit(Function var1) {

        ExpressionList params=var1.getParameters();
        List<Object> args = new ArrayList<>();
        WhereExprVisitor1 tmp = new WhereExprVisitor1("tmp");
        tmp.fieldValues=this.fieldValues;
        params.getExpressions().forEach(expr->{
            expr.accept(tmp);
            args.add(tmp.result);
        });
        result= UdfCache.cache.get(var1.getName()).eval(args);
        //System.out.println(args);
        //System.out.println(FunctionCache.cache);
    }

}

