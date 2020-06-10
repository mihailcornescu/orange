package com.orange.api.service;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.orange.api.model.Transaction;

public interface TransactionStoreService {

    Transaction createTransaction(@RequestBody Transaction body);

    /**
     * Sample usage: curl $HOST:$PORT/transaction/1
     *
     * @param transactionId
     * @return the transaction, if found, else null
     */
    @GetMapping(
        value    = "/transaction/{transactionId}",
        produces = "application/json")
     Mono<Transaction> getTransaction(@PathVariable int transactionId);

    void deleteTransaction(@PathVariable int transactionId);
}