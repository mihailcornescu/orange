package com.orange.api.service;

import com.orange.api.model.Transaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Api(description = "REST API for transaction creation.")
public interface TransactionReadService {

    /**
     * Sample usage:
     *
     * curl -X POST $HOST:$PORT/create-transaction \
     *   -H "Content-TransactionType: application/json" --data \
     *   '{"transactionId":123,"name":"ion popescu","amount":123}'
     *
     * @param body
     */
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @PostMapping(
        value    = "/create-transaction",
        consumes = "application/json")
    void createTransaction(@Valid @RequestBody Transaction body);

}