package com.orange.microservices.transaction.read;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.api.event.Event;
import com.orange.api.model.Transaction;
import com.orange.api.model.TransactionType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.orange.api.event.Event.Type.CREATE;
import static com.orange.api.event.Event.Type.DELETE;

public class IsSameEventTests {

	ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEventObjectCompare() throws JsonProcessingException {

    	// Event #1 and #2 are the same event, but occurs as different times
		// Event #3 and #4 are different events
		Event<Integer, Transaction> event1 = new Event<>(CREATE, 1, new Transaction(1, TransactionType.IBAN_TO_IBAN, "iban", "cnp", "name", "desc", 1));
		Event<Integer, Transaction> event2 = new Event<>(CREATE, 1, new Transaction(1, TransactionType.IBAN_TO_IBAN, "iban", "cnp", "name", "desc", 1));
		Event<Integer, Transaction> event3 = new Event<>(DELETE, 1, null);
		Event<Integer, Transaction> event4 = new Event<>(CREATE, 1, new Transaction(2, TransactionType.IBAN_TO_IBAN, "iban", "cnp", "name", "desc", 1));

		String event1JSon = mapper.writeValueAsString(event1);

		MatcherAssert.assertThat(event1JSon, Matchers.is(IsSameEvent.sameEventExceptCreatedAt(event2)));
		MatcherAssert.assertThat(event1JSon, Matchers.not(IsSameEvent.sameEventExceptCreatedAt(event3)));
		MatcherAssert.assertThat(event1JSon, Matchers.not(IsSameEvent.sameEventExceptCreatedAt(event4)));
    }
}
