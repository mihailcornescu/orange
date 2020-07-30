package com.orange.microservices.transaction.read.services;

import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionReadService;
import com.orange.microservices.transaction.read.validation.TransactionFieldsValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionReadServiceImpl implements TransactionReadService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionReadServiceImpl.class);

    private final TransactionReadCompositeIntegration integration;

    TransactionFieldsValidationService validationService;

    @Autowired
    public TransactionReadServiceImpl(TransactionReadCompositeIntegration integration,
                                      TransactionFieldsValidationService validationService) {
        this.integration = integration;
        this.validationService = validationService;
    }

    @Override
    public void createTransaction(Transaction body) {

        try {
            LOG.debug("createTransaction: creates a new transaction for transactionId: {}", body.getTransactionId());

            if (validationService.areAllTransactionFieldsValid(body)) {
                integration.createTransaction(body);
            }

            LOG.debug("createTransaction: transaction created for transactionId: {}", body.getTransactionId());

        } catch (RuntimeException re) {
            LOG.warn("createTransaction failed: {}", re.toString());
            throw re;
        }
    }

}
