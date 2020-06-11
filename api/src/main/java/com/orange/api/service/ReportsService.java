package com.orange.api.service;

import com.orange.api.model.ReportFull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface ReportsService {

    /**
     * Sample usage: curl $HOST:$PORT/transactions?cnp=1801121240214
     *
     * @return the report
     */
    @GetMapping(
            value    = "/transactions",
            produces = "application/json")
    Mono<ReportFull> getTransactions(@RequestParam(value = "cnp", required = true) String cnp);
}
