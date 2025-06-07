package com.tanpp.stream;

public class Transaction {
    public static final String SUCCESS = "0";
    private long duration;
    private final long startTs = System.currentTimeMillis();
    private Throwable t;
    private String status;

    public Transaction(String name, String subName) {
    }


    public void complete() {
        duration = System.currentTimeMillis() - startTs;
        // metric writes to time series db
    }

    public void setStatus(Throwable t) {
        this.t = t;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
