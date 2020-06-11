package com.orange.microservices.transaction.services;

import com.orange.api.model.ReportFull;
import com.orange.api.model.ReportPerType;
import com.orange.api.model.Transaction;
import com.orange.api.service.ReportsService;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.review.Review;

import java.util.List;

@RestController
public class ReportsServiceImpl implements ReportsService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportsServiceImpl.class);

    private final TransactionRepository repository;

    private final TransactionMapper mapper;

    @Autowired
    public ReportsServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ReportFull> getTransactions(String cnp) {
        return Mono.zip(
                values -> buildReportFull((List<Transaction>) values[0]),
                repository.findByCnp(cnp).collectList())
                .doOnError(ex -> LOG.warn("creating report failed"))
                .log();
    }

    private ReportFull buildReportFull(List<Transaction> transactions) {
        ReportFull reportFull = new ReportFull();
        return reportFull;
    }

    private void processTransaction(ReportFull reportFull, TransactionEntity entity) {
        if (reportFull.getCnp() == null) {
            reportFull.setCnp(entity.getCnp());
        }
        if (reportFull.getIban() == null) {
            reportFull.setIban(entity.getIban());
        }
        if (reportFull.getName() == null) {
            reportFull.setName(entity.getName());
        }
        ReportPerType ibanToIban = reportFull.getIbanToIban();
        ReportPerType ibanToWallet = reportFull.getIbanToWallet();
        ReportPerType walletToIban = reportFull.getWalletToIban();
        ReportPerType walletToWallet = reportFull.getWalletToWallet();
        switch (entity.getType()) {
            case IBAN_TO_IBAN: this.buildReportPerType(ibanToIban, entity); break;
            case WALLET_TO_IBAN: this.buildReportPerType(walletToIban, entity); break;
            case IBAN_TO_WALLET: this.buildReportPerType(ibanToWallet, entity); break;
            case WALLET_TO_WALLET: this.buildReportPerType(walletToWallet, entity); break;
        }
    }

    private void buildReportPerType(ReportPerType reportPerType, TransactionEntity entity) {
        reportPerType.setTotalNumber(reportPerType.getTotalNumber() + 1);
        reportPerType.setTotalSum(reportPerType.getTotalSum() + entity.getAmount());
        reportPerType.getTransactions().add(mapper.entityToApi(entity));
    }

}
