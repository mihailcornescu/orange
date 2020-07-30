package com.orange.microservices.transaction.read.validation;

import com.orange.api.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class CNPValidationServiceImpl {

    boolean areCNPsValid(Transaction transactionDto) {
//        String payerCNP = transactionDto.getCnpPayer();
//        String payeeCNP = transactionDto.getCnpPayee();
//
//        boolean isPayerCNPValid = isCNPValid(payerCNP);
//        boolean isPayeeCNPValid = isCNPValid(payeeCNP);
//
//        if (isPayerCNPValid && isPayeeCNPValid) {
//            return true;
//        }
//
//        return false;
        return isCNPValid(transactionDto.getCnp());
    }

    boolean isCNPValid(String cnp) {
        long digitControl = calculateDigitControl(cnp);
        int lastDigitOfIban = cnp.charAt(cnp.length() - 1) - '0';
        if (digitControl == lastDigitOfIban) {
            return true;
        }
        return false;
    }

    public long calculateDigitControl(String cnp) {
        long sumControl = 0;
        String controlTemplate = "279146358279";
        //multiply each digit and make the sum
        for (int i = 0; i < cnp.length() - 1; i++) {
                int cnpCurrentDigit = cnp.charAt(i) - '0';
                int controlTemplateCurrentDigit = controlTemplate.charAt(i) - '0';
                sumControl += cnpCurrentDigit * controlTemplateCurrentDigit;
        }

        long sumControlRest = sumControl % 11;
        long digitControl;
        if (sumControlRest == 10) {
            digitControl = 1;
        } else {
            digitControl = sumControlRest;
        }
        return digitControl;
    }
}
