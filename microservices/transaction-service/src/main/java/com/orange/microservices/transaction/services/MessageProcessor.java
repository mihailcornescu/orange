package com.orange.microservices.transaction.services;

import com.orange.api.event.Event;
import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionStoreService;
import com.orange.util.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final TransactionStoreService service;

    @Autowired
    public MessageProcessor(TransactionStoreService service) {
        this.service = service;
    }

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Transaction> event) {

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

        case CREATE:
            Transaction transaction = event.getData();
            LOG.info("Create transaction with ID: {}", transaction.getTransactionId());
            service.createTransaction(transaction);
            break;

        default:
            String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
            LOG.warn(errorMessage);
            throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
