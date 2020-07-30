package com.orange.microservices.transaction.read.validation;

import com.orange.api.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionFieldsValidationService {

    @Autowired
    CNPValidationServiceImpl cnpValidationService;

    @Autowired
    IbanValidationServiceImpl ibanValidationService;

    public boolean areAllTransactionFieldsValid(Transaction transaction) {
        return transaction.getAmount() > 0 &&
            transaction.getDescription() != null; //&&
//            transaction.getNamePayer() != null &&
//            transaction.getNamePayee() != null &&
//            transaction.getTransactionType() != null &&
//            cnpValidationService.areCNPsValid(transaction) &&
//                ibanValidationService.areIbansValid(transaction)

    }
}
