package com.orange.microservices.transaction;

import com.orange.api.model.TransactionType;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.orange")
public class TransactionServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceApplication.class);

	static TransactionRepository repository;

	@Autowired
	TransactionServiceApplication(TransactionRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(TransactionServiceApplication.class, args);

		String mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		LOG.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort);

		dbSetup();
	}

	private static void dbSetup() {
		List<TransactionEntity> transactions = new ArrayList<>();
		transactions.add(new TransactionEntity(1, TransactionType.IBAN_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "plata chirie", 500));
		transactions.add(new TransactionEntity(2, TransactionType.IBAN_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "donatie", 150));
		transactions.add(new TransactionEntity(3, TransactionType.IBAN_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "trimis bani prieten", 200));
		transactions.add(new TransactionEntity(4, TransactionType.WALLET_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "cumparaturi haine", 300));
		transactions.add(new TransactionEntity(1, TransactionType.WALLET_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "cumparaturi mancare", 200));

		transactions.forEach(t -> repository.save(t));
	}
}
