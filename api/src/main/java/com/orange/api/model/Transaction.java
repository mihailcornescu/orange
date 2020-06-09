package com.orange.api.model;

public class Transaction {
    private int transactionId;
    private String name;
    private int amount;

    public Transaction() {
        transactionId = 0;
        name = null;
        amount = 0;
    }

    public Transaction(int transactionId, String name, int amount) {
        this.transactionId = transactionId;
        this.name = name;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
