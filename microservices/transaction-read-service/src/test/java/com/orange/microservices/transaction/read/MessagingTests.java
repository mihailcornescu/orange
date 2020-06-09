package com.orange.microservices.transaction.read;

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
import com.orange.api.model.Transaction;
import com.orange.api.event.Event;
import com.orange.microservices.transaction.read.services.TransactionStoreCompositeIntegration;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;
import static org.springframework.http.HttpStatus.OK;
import static reactor.core.publisher.Mono.just;
import static com.orange.api.event.Event.Type.CREATE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)

public class MessagingTests {

	@Autowired
    private WebTestClient client;

	@Autowired
	private TransactionStoreCompositeIntegration.MessageSources channels;

	@Autowired
	private MessageCollector collector;

	BlockingQueue<Message<?>> queueProducts = null;

	@Before
	public void setUp() {
		queueProducts = getQueue(channels.outputProducts());
	}

	@Test
	public void createCompositeProduct1() {

		Transaction composite = new Transaction(1, "name", 1);
		postAndVerifyProduct(composite, OK);

		// Assert one expected new product events queued up
		assertEquals(1, queueProducts.size());

		Event<Integer, Transaction> expectedEvent = new Event(CREATE, composite.getTransactionId(), new Transaction(composite.getTransactionId(), composite.getName(), composite.getAmount()));
		assertThat(queueProducts, Matchers.is(receivesPayloadThat(IsSameEvent.sameEventExceptCreatedAt(expectedEvent))));
	}

	@Test
	public void createCompositeProduct2() {

		Transaction transaction = new Transaction(1, "name", 1);

		postAndVerifyProduct(transaction, OK);

		// Assert one create transaction event queued up
		assertEquals(1, queueProducts.size());

		Event<Integer, Transaction> expectedProductEvent = new Event(CREATE, transaction.getTransactionId(), new Transaction(transaction.getTransactionId(), transaction.getName(), transaction.getAmount()));
		assertThat(queueProducts, receivesPayloadThat(IsSameEvent.sameEventExceptCreatedAt(expectedProductEvent)));
	}

	private BlockingQueue<Message<?>> getQueue(MessageChannel messageChannel) {
		return collector.forChannel(messageChannel);
	}

	private void postAndVerifyProduct(Transaction transaction, HttpStatus expectedStatus) {
		client.post()
			.uri("/create-transaction")
			.body(just(transaction), Transaction.class)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus);
	}

}