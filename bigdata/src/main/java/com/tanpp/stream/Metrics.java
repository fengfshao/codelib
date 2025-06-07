package com.tanpp.stream;


public class Metrics {
    public static Transaction newTransaction(String name, String subName) {
        return new Transaction(name, subName);
    }
}