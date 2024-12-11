package com.shaoff.dig.basic;

import java.util.function.DoubleBinaryOperator;

public enum  Operation {
    PLUS("+",(x,y)->x+y),
    MINUS("-",(x,y)->x-y);

    private final String symbol;
    private final DoubleBinaryOperator op;
    Operation(String symbol,DoubleBinaryOperator op){
        this.symbol=symbol;
        this.op=op;
    }

    public double apply(double x,double y){
        return op.applyAsDouble(x,y);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "symbol='" + symbol + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Operation op=Operation.MINUS;
        System.out.println(op.apply(1,2));
        System.out.println(op);
    }
}
