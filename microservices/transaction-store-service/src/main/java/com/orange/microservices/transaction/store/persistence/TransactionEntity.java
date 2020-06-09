package com.orange.microservices.transaction.store.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.lang.String.format;

@Document(collection="products")
public class TransactionEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    @Indexed(unique = true)
    private int transactionId;

    private String name;
    private int amount;

    public TransactionEntity() {
    }

    public TransactionEntity(int transactionId, String name, int amount) {
        this.transactionId = transactionId;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return format("TransactionEntity: %s", transactionId);
    }

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
