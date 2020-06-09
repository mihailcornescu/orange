package com.orange.microservices.transaction.store.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, String> {
    Mono<TransactionEntity> findByTransactionId(int productId);
}
