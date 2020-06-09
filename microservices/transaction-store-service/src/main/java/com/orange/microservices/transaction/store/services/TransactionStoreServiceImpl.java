package com.orange.microservices.transaction.store.services;

import com.orange.microservices.transaction.store.persistence.TransactionEntity;
import com.orange.microservices.transaction.store.persistence.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionStoreService;
import com.orange.util.exceptions.InvalidInputException;
import com.orange.util.exceptions.NotFoundException;
import com.orange.util.http.ServiceUtil;

import static reactor.core.publisher.Mono.error;

@RestController
public class TransactionStoreServiceImpl implements TransactionStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionStoreServiceImpl.class);

    private final ServiceUtil serviceUtil;

    private final TransactionRepository repository;

    private final TransactionMapper mapper;

    @Autowired
    public TransactionStoreServiceImpl(TransactionRepository repository, TransactionMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
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

    @Override
    public Mono<Transaction> getTransaction(int transactionId) {

        if (transactionId < 1) throw new InvalidInputException("Invalid transactionId: " + transactionId);

        return repository.findByTransactionId(transactionId)
            .switchIfEmpty(error(new NotFoundException("No product found for transactionId: " + transactionId)))
            .log()
            .map(e -> mapper.entityToApi(e));
    }

    @Override
    public void deleteTransaction(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        LOG.debug("deleteTransaction: tries to delete an entity with productId: {}", productId);
        repository.findByTransactionId(productId).log().map(e -> repository.delete(e)).flatMap(e -> e).block();
    }
}