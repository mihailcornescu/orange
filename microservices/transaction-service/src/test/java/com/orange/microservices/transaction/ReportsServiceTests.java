package com.orange.microservices.transaction;

import com.orange.api.model.TransactionType;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class ReportsServiceTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private TransactionRepository repository;

    @Before
    public void setupDb() {
        List<TransactionEntity> transactions = new ArrayList<>();
        transactions.add(new TransactionEntity(1, TransactionType.IBAN_TO_IBAN, "i", "c", "n", "d1", 1));
        transactions.add(new TransactionEntity(2, TransactionType.IBAN_TO_IBAN, "i", "c", "n", "d2", 2));
        transactions.add(new TransactionEntity(3, TransactionType.IBAN_TO_IBAN, "i", "c", "n", "d3", 3));
        transactions.add(new TransactionEntity(4, TransactionType.WALLET_TO_IBAN, "i", "c", "n", "d4", 4));
        transactions.add(new TransactionEntity(5, TransactionType.WALLET_TO_IBAN, "i", "c", "n", "d4", 5));

        for (TransactionEntity entity : transactions) {
            StepVerifier.create(repository.save(entity))
                    .expectNextMatches(createdEntity -> entity.getTransactionId() == createdEntity.getTransactionId())
                    .verifyComplete();

        }
    }

    @Test
    public void getReport() {
        String cnp = "c";
        String iban = "i";
        getAndVerifyReport(cnp, OK)
                .jsonPath("$.cnp").isEqualTo(cnp)
                .jsonPath("$.iban").isEqualTo(iban)
                .jsonPath("$.ibanToIban.type").isEqualTo(TransactionType.IBAN_TO_IBAN.toString())
                .jsonPath("$.ibanToIban.totalNumber").isEqualTo(3)
                .jsonPath("$.ibanToIban.totalSum").isEqualTo(6)
                .jsonPath("$.ibanToIban.transactions.length()").isEqualTo(3);
    }

    private WebTestClient.BodyContentSpec getAndVerifyReport(String cnp, HttpStatus expectedStatus) {
        return client.get()
                .uri("/transactions?cnp=" + cnp)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }
}
