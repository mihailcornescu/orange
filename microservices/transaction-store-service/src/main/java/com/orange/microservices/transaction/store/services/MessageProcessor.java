package com.orange.microservices.transaction.store.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionStoreService;
import com.orange.api.event.Event;
import com.orange.util.exceptions.EventProcessingException;

@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final TransactionStoreService transactionStoreService;

    @Autowired
    public MessageProcessor(TransactionStoreService transactionStoreService) {
        this.transactionStoreService = transactionStoreService;
    }

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Transaction> event) {

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

        case CREATE:
            Transaction transaction = event.getData();
            LOG.info("Create transaction with ID: {}", transaction.getTransactionId());
            transactionStoreService.createTransaction(transaction);
            break;

        case DELETE:
            int transactionId = event.getKey();
            LOG.info("Delete recommendations with TransactionID: {}", transactionId);
            transactionStoreService.deleteTransaction(transactionId);
            break;

        default:
            String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
            LOG.warn(errorMessage);
            throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
