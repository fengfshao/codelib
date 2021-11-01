package me.fengfshao.sqlparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSqlParserHelper {

    private static final Logger log = LoggerFactory.getLogger(JSqlParserHelper.class);
    private static final String className = "[JSqlParserHelper] ";

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
            log.error(e.getMessage());
            return null;
        }
    }
}
