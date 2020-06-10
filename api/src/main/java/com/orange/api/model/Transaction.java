package com.orange.api.model;

public class Transaction {
    private int transactionId;
    private String type;
    private String iban;
    private String cnp;
    private String name;
    private String description;
    private int amount;

    public Transaction() {
        transactionId = 0;
        type = null;
        iban = null;
        cnp = null;
        name = null;
        description = null;
        amount = 0;
    }

    public Transaction(int transactionId, String type, String iban, String cnp, String name, String description, int amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.iban = iban;
        this.cnp = cnp;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
