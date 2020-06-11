package com.orange.microservices.transaction;

import com.orange.api.event.Event;
import com.orange.api.model.Transaction;
import com.orange.api.model.TransactionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.orange.microservices.transaction.persistence.TransactionRepository;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class TransactionServiceApplicationTests {

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
	public void createTransaction() {

		int transactionId = 1;

		assertNull(repository.findByTransactionId(transactionId).block());
		assertEquals(0, (long)repository.count().block());

		sendCreateTransactionEvent(transactionId);

		assertNotNull(repository.findByTransactionId(transactionId).block());
		assertEquals(1, (long)repository.count().block());

	}

	private void sendCreateTransactionEvent(int transactionId) {
		Transaction product = new Transaction(transactionId, TransactionType.IBAN_TO_IBAN, "Iban " + transactionId, "Cnp" + transactionId, "Name " + transactionId, "Desc " + transactionId, transactionId);
		Event<Integer, Transaction> event = new Event(Event.Type.CREATE, transactionId, product);
		input.send(new GenericMessage<>(event));
	}

}