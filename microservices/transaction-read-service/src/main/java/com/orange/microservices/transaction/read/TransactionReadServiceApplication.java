package com.orange.microservices.transaction.read;

import com.orange.microservices.transaction.read.services.TransactionStoreCompositeIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.LinkedHashMap;

@EnableSwagger2WebFlux
@SpringBootApplication
@ComponentScan("com.orange")
public class TransactionReadServiceApplication {

	@Autowired
	HealthAggregator healthAggregator;

	@Autowired
    TransactionStoreCompositeIntegration integration;

	@Bean
	ReactiveHealthIndicator coreServices() {

		ReactiveHealthIndicatorRegistry registry = new DefaultReactiveHealthIndicatorRegistry(new LinkedHashMap<>());

		registry.register("transaction-store", () -> integration.getTransactionStoreHealth());

		return new CompositeReactiveHealthIndicator(healthAggregator, registry);
	}

	public static void main(String[] args) {
		SpringApplication.run(TransactionReadServiceApplication.class, args);
	}
}
