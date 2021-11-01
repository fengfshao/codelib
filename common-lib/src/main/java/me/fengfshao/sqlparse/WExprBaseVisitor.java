package me.fengfshao.sqlparse;

import java.io.Serializable;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.statement.select.SubSelect;

public abstract class WExprBaseVisitor implements ExpressionVisitor, ItemsListVisitor,
        Serializable {

    @Override
    public void visit(Function var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(JdbcParameter var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(JdbcNamedParameter var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(SubSelect var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(CaseExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(WhenClause var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(ExistsExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(AllComparisonExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(AnyComparisonExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(Matches var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(BitwiseAnd var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(BitwiseOr var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(BitwiseXor var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(CastExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(AnalyticExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(ExtractExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(IntervalExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(OracleHierarchicalExpression var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(RegExpMatchOperator var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(Between var1) {
        throw new UnsupportedOperationException("not supported!");
    }

    @Override
    public void visit(MultiExpressionList var1) {
        throw new UnsupportedOperationException("not supported!");
    }
}
