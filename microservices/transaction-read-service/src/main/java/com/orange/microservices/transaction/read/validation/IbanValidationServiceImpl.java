package com.orange.microservices.transaction.read.validation;

import com.orange.api.model.Transaction;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.stereotype.Service;

@Service
public class IbanValidationServiceImpl {

    IBANValidator ibanValidator = IBANValidator.getInstance();

    public boolean areIbansValid(Transaction transactionDto) {
//        String payerIban = transactionDto.getIbanPayer();
//        String payeeIban = transactionDto.getIbanPayee();
//
//        boolean isPayerIbanValid = isIbanValid(payerIban);
//        boolean isPayeeIbanValid = isIbanValid(payeeIban);
//
//        if (isPayerIbanValid && isPayeeIbanValid) {
//            return true;
//        }
//
//        return false;
        return isIbanValid(transactionDto.getIban());
    }

    private boolean isIbanValid(String iban) {
        if (ibanValidator.isValid(iban)) {
            return true;
        }

        return false;
    }
}

