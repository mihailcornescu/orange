package com.orange.microservices.transaction.read.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionReadService;
import com.orange.util.http.ServiceUtil;

@RestController
public class TransactionReadServiceImpl implements TransactionReadService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionReadServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final TransactionStoreCompositeIntegration integration;

    @Autowired
    public TransactionReadServiceImpl(ServiceUtil serviceUtil, TransactionStoreCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public void createTransaction(Transaction body) {

        try {

            LOG.debug("createTransaction: creates a new composite entity for productId: {}", body.getTransactionId());

            integration.createTransaction(body);

            LOG.debug("createTransaction: composite entities created for productId: {}", body.getTransactionId());

        } catch (RuntimeException re) {
            LOG.warn("createTransaction failed: {}", re.toString());
            throw re;
        }
    }

}