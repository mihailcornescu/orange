package com.orange.microservices.transaction.store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.orange.api.model.Transaction;
import com.orange.api.event.Event;
import com.orange.microservices.transaction.store.persistence.TransactionRepository;
import com.orange.util.exceptions.InvalidInputException;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static com.orange.api.event.Event.Type.CREATE;
import static com.orange.api.event.Event.Type.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class TransactionStoreServiceApplicationTests {

    @Autowired
    private WebTestClient client;

	@Autowired
	private TransactionRepository repository;

	@Autowired
	private Sink channels;

	private AbstractMessageChannel input = null;

	@Before
	public void setupDb() {
		input = (AbstractMessageChannel) channels.input();
		repository.deleteAll().block();
	}

	@Test
	public void getTransactionById() {

		int transactionId = 1;

		assertNull(repository.findByTransactionId(transactionId).block());
		assertEquals(0, (long)repository.count().block());

		sendCreateTransactionEvent(transactionId);

		assertNotNull(repository.findByTransactionId(transactionId).block());
		assertEquals(1, (long)repository.count().block());

		getAndVerifyTransaction(transactionId, OK)
            .jsonPath("$.transactionId").isEqualTo(transactionId);
	}

	@Test
	public void duplicateError() {

		int transactionId = 1;

		assertNull(repository.findByTransactionId(transactionId).block());

		sendCreateTransactionEvent(transactionId);

		assertNotNull(repository.findByTransactionId(transactionId).block());

		try {
			sendCreateTransactionEvent(transactionId);
			fail("Expected a MessagingException here!");
		} catch (MessagingException me) {
			if (me.getCause() instanceof InvalidInputException)	{
				InvalidInputException iie = (InvalidInputException)me.getCause();
				assertEquals("Duplicate key, Transaction Id: " + transactionId, iie.getMessage());
			} else {
				fail("Expected a InvalidInputException as the root cause!");
			}
		}
	}

	@Test
	public void deleteTransaction() {

		int transactionId = 1;

		sendCreateTransactionEvent(transactionId);
		assertNotNull(repository.findByTransactionId(transactionId).block());

		sendDeleteTransactionEvent(transactionId);
		assertNull(repository.findByTransactionId(transactionId).block());

		sendDeleteTransactionEvent(transactionId);
	}

	@Test
	public void getTransactionInvalidParameterString() {

		getAndVerifyTransaction("/no-integer", BAD_REQUEST)
            .jsonPath("$.path").isEqualTo("/transaction/no-integer")
            .jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getTransactionNotFound() {

		int transactionIdNotFound = 13;
		getAndVerifyTransaction(transactionIdNotFound, NOT_FOUND)
            .jsonPath("$.path").isEqualTo("/transaction/" + transactionIdNotFound)
            .jsonPath("$.message").isEqualTo("No product found for transactionId: " + transactionIdNotFound);
	}

	@Test
	public void getTransactionInvalidParameterNegativeValue() {

        int transactionIdInvalid = -1;

		getAndVerifyTransaction(transactionIdInvalid, UNPROCESSABLE_ENTITY)
            .jsonPath("$.path").isEqualTo("/transaction/" + transactionIdInvalid)
            .jsonPath("$.message").isEqualTo("Invalid transactionId: " + transactionIdInvalid);
	}

	private WebTestClient.BodyContentSpec getAndVerifyTransaction(int transactionId, HttpStatus expectedStatus) {
		return getAndVerifyTransaction("/" + transactionId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyTransaction(String productIdPath, HttpStatus expectedStatus) {
		return client.get()
			.uri("/transaction" + productIdPath)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
	}

	private void sendCreateTransactionEvent(int productId) {
		Transaction transaction = new Transaction(productId, "Name " + productId, productId);
		Event<Integer, Transaction> event = new Event(CREATE, productId, transaction);
		input.send(new GenericMessage<>(event));
	}

	private void sendDeleteTransactionEvent(int productId) {
		Event<Integer, Transaction> event = new Event(DELETE, productId, null);
		input.send(new GenericMessage<>(event));
	}
}