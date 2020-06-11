package com.orange.microservices.transaction.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, String> {
    Mono<TransactionEntity> findByTransactionId(int transactionId);
    Flux<TransactionEntity> findByCnp(String cnp);
}
