package com.orange.microservices.transaction;

import com.github.mongobee.Mongobee;
import com.orange.api.model.TransactionType;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.orange")
public class TransactionServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceApplication.class);

	@Autowired
	MongoTemplate mongoTemplate;

	static String mongodDbHost;
	static String mongodDbPort;
	static String mongodDbDatabase;

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(TransactionServiceApplication.class, args);

		mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		mongodDbDatabase = ctx.getEnvironment().getProperty("spring.data.mongodb.database");
		LOG.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort);
	}

	@Bean
	public Mongobee mongobee(){
		Mongobee runner = new Mongobee("mongodb://localhost:27017/transactions-db");
		runner.setMongoTemplate(mongoTemplate);
		runner.setChangeLogsScanPackage(
				"com.orange.microservices.transactions.changelogs"); // the package to be scanned for changesets

		return runner;
	}

}
