package com.orange.microservices.transaction.read;

import com.orange.api.event.Event;
import com.orange.api.model.Transaction;
import com.orange.api.model.TransactionType;
import com.orange.microservices.transaction.read.services.TransactionReadCompositeIntegration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.BlockingQueue;

import static com.orange.api.event.Event.Type.CREATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;
import static org.springframework.http.HttpStatus.OK;
import static reactor.core.publisher.Mono.just;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)

public class MessagingTests {

	@Autowired
    private WebTestClient client;

	@Autowired
	private TransactionReadCompositeIntegration.MessageSources channels;

	@Autowired
	private MessageCollector collector;

	BlockingQueue<Message<?>> queueTransactions = null;

	@Before
	public void setUp() {
		queueTransactions = getQueue(channels.outputTransactions());
	}

	@Test
	public void createTransaction1() {

		Transaction transaction = new Transaction(1, TransactionType.IBAN_TO_IBAN, "iban", "cnp", "name", "desc", 1);
		postAndVerifyTransaction(transaction, OK);

		// Assert one expected new transaction events queued up
		assertEquals(1, queueTransactions.size());

		Event<Integer, Transaction> expectedEvent = new Event(CREATE, transaction.getTransactionId(), new Transaction(transaction.getTransactionId(), transaction.getType(), transaction.getIban(), transaction.getCnp(), transaction.getName(), transaction.getDescription(), transaction.getAmount()));
		assertThat(queueTransactions, Matchers.is(receivesPayloadThat(IsSameEvent.sameEventExceptCreatedAt(expectedEvent))));
	}

	@Test
	public void createTransaction2() {

		Transaction transaction = new Transaction(1, TransactionType.IBAN_TO_IBAN, "iban", "cnp", "name", "desc", 1);

		postAndVerifyTransaction(transaction, OK);

		// Assert one create transaction event queued up
		assertEquals(1, queueTransactions.size());

		Event<Integer, Transaction> expectedTransactionEvent = new Event(CREATE, transaction.getTransactionId(), new Transaction(transaction.getTransactionId(), transaction.getType(), transaction.getIban(), transaction.getCnp(), transaction.getName(), transaction.getDescription(), transaction.getAmount()));
		assertThat(queueTransactions, receivesPayloadThat(IsSameEvent.sameEventExceptCreatedAt(expectedTransactionEvent)));
	}

	private BlockingQueue<Message<?>> getQueue(MessageChannel messageChannel) {
		return collector.forChannel(messageChannel);
	}

	private void postAndVerifyTransaction(Transaction transaction, HttpStatus expectedStatus) {
		client.post()
			.uri("/create-transaction")
			.body(just(transaction), Transaction.class)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus);
	}

}
