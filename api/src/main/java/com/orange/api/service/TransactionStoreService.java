package com.orange.api.service;

import com.orange.api.model.Transaction;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionStoreService {

    Transaction createTransaction(@RequestBody Transaction body);

}