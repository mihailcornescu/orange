package com.orange.microservices.transaction.persistence;

import com.orange.api.model.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.lang.String.format;

@Document(collection="transactions")
public class TransactionEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    @Indexed(unique = true)
    private int transactionId;

    private TransactionType type;
    private String iban;
    private String cnp;
    private String name;
    private String description;
    private int amount;

    public TransactionEntity() {
    }

    public TransactionEntity(int transactionId, TransactionType type, String iban, String cnp, String name, String description, int amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.iban = iban;
        this.cnp = cnp;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return format("TransactionEntity: %s", transactionId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
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
