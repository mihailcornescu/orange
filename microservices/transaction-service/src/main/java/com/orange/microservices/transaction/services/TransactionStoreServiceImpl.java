package com.orange.microservices.transaction.services;

import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionStoreService;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import com.orange.util.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransactionStoreServiceImpl implements TransactionStoreService {

    private final TransactionRepository repository;

    private final TransactionMapper mapper;

    @Autowired
    public TransactionStoreServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Transaction createTransaction(Transaction body) {

        if (body.getTransactionId() < 1) throw new InvalidInputException("Invalid productId: " + body.getTransactionId());

        TransactionEntity entity = mapper.apiToEntity(body);
        Mono<Transaction> newEntity = repository.save(entity)
            .log()
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Transaction Id: " + body.getTransactionId()))
            .map(e -> mapper.entityToApi(e));

        return newEntity.block();
    }

}