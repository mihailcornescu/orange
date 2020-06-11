package com.orange.api.service;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.orange.api.model.Transaction;

public interface TransactionStoreService {

    Transaction createTransaction(@RequestBody Transaction body);

}