package com.orange.microservices.transaction.services;

import com.orange.api.model.ReportFull;
import com.orange.api.model.ReportPerType;
import com.orange.api.model.Transaction;
import com.orange.api.model.TransactionType;
import com.orange.api.service.ReportsService;
import com.orange.microservices.transaction.persistence.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static reactor.core.publisher.Flux.empty;

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
                getTransactionsFromDb(cnp).collectList())
                .doOnError(ex -> empty())
                .log();
    }

    private Flux<Transaction> getTransactionsFromDb(String cnp) {
        return repository.findByCnp(cnp)
            .map(t -> mapper.entityToApi(t))
            .log()
            .onErrorResume(error -> empty());
    }

    private ReportFull buildReportFull(List<Transaction> transactions) {
        ReportFull reportFull = new ReportFull();
        if (CollectionUtils.isEmpty(transactions)) {
            return reportFull;
        }
        for(Transaction transaction : transactions) {
            processTransaction(reportFull, transaction);
        }
        return reportFull;
    }

    private void processTransaction(ReportFull reportFull, Transaction transaction) {
        if (reportFull.getCnp() == null) {
            reportFull.setCnp(transaction.getCnp());
        }
        if (reportFull.getIban() == null) {
            reportFull.setIban(transaction.getIban());
        }
        if (reportFull.getName() == null) {
            reportFull.setName(transaction.getName());
        }
        switch (transaction.getType()) {
            case IBAN_TO_IBAN: reportFull.getIbanToIban().setType(TransactionType.IBAN_TO_IBAN); this.buildReportPerType(reportFull.getIbanToIban(), transaction); break;
            case WALLET_TO_IBAN: reportFull.getWalletToIban().setType(TransactionType.WALLET_TO_IBAN); this.buildReportPerType(reportFull.getWalletToIban(), transaction); break;
            case IBAN_TO_WALLET: reportFull.getIbanToWallet().setType(TransactionType.IBAN_TO_WALLET); this.buildReportPerType(reportFull.getIbanToWallet(), transaction); break;
            case WALLET_TO_WALLET: reportFull.getWalletToWallet().setType(TransactionType.WALLET_TO_WALLET); this.buildReportPerType(reportFull.getWalletToWallet(), transaction); break;
        }
    }

    private void buildReportPerType(ReportPerType reportPerType, Transaction transaction) {
        reportPerType.setTotalNumber(reportPerType != null ? reportPerType.getTotalNumber() + 1 : 0);
        reportPerType.setTotalSum(reportPerType != null ? reportPerType.getTotalSum() + transaction.getAmount() : 0);
        reportPerType.getTransactions().add(transaction);
    }

}
