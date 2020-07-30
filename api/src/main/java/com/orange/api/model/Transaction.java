package com.orange.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private int transactionId;
    private TransactionType type;
    private String iban;
    private String cnp;
    private String name;
    private String description;
    private int amount;
}
