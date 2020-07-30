package com.orange.microservices.transaction.persistence;

import com.orange.api.model.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.lang.String.format;

@Data
@NoArgsConstructor
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

    public TransactionEntity(int transactionId, TransactionType type, String iban, String cnp, String name, String description, int amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.iban = iban;
        this.cnp = cnp;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }
}
