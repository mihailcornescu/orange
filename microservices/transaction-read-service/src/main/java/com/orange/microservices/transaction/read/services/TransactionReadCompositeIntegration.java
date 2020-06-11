package com.orange.microservices.transaction.read.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import com.orange.api.model.Transaction;
import com.orange.api.service.TransactionStoreService;
import com.orange.api.event.Event;
import com.orange.util.exceptions.InvalidInputException;
import com.orange.util.exceptions.NotFoundException;
import com.orange.util.http.HttpErrorInfo;

import java.io.IOException;

import static com.orange.api.event.Event.Type.CREATE;

@EnableBinding(TransactionReadCompositeIntegration.MessageSources.class)
@Component
public class TransactionReadCompositeIntegration implements TransactionStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionReadCompositeIntegration.class);

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String transactionStoreServiceUrl;

    private MessageSources messageSources;

    public interface MessageSources {

        String OUTPUT_TRANSACTIONS = "output-transactions";

        @Output(OUTPUT_TRANSACTIONS)
        MessageChannel outputTransactions();

    }

    @Autowired
    public TransactionReadCompositeIntegration(
        WebClient.Builder webClient,
        ObjectMapper mapper,
        MessageSources messageSources,

        @Value("${app.transaction-service.host}") String transactionStoreServiceHost,
        @Value("${app.transaction-service.port}") int    transactionStoreServicePort
    ) {

        this.webClient = webClient.build();
        this.mapper = mapper;
        this.messageSources = messageSources;

        transactionStoreServiceUrl = "http://" + transactionStoreServiceHost + ":" + transactionStoreServicePort;
    }

    @Override
    public Transaction createTransaction(Transaction body) {
        messageSources.outputTransactions().send(MessageBuilder.withPayload(new Event(CREATE, body.getTransactionId(), body)).build());
        return body;
    }

    @Override
    public Mono<Transaction> getTransaction(int transactionId) {
        return null;
    }

    public Mono<Health> getTransactionStoreHealth() {
        return getHealth(transactionStoreServiceUrl);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
            .map(s -> new Health.Builder().up().build())
            .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
            .log();
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException)ex;

        switch (wcre.getStatusCode()) {

        case NOT_FOUND:
            return new NotFoundException(getErrorMessage(wcre));

        case UNPROCESSABLE_ENTITY :
            return new InvalidInputException(getErrorMessage(wcre));

        default:
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
            LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
            return ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}