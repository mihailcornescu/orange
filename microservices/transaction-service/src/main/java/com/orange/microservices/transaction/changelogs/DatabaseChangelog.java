package com.orange.microservices.transaction.changelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.DB;
import com.orange.api.model.TransactionType;
import com.orange.microservices.transaction.persistence.TransactionEntity;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "populateDb", author = "Mihail Cornescu")
    public void populateDb(MongoTemplate template){
        List<TransactionEntity> transactions = new ArrayList<>();
        transactions.add(new TransactionEntity(1, TransactionType.IBAN_TO_IBAN, "RO01RNC1B00000000123456789", "1801203250054", "ion popescu", "plata chirie", 500));
        transactions.add(new TransactionEntity(2, TransactionType.IBAN_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "donatie", 150));
        transactions.add(new TransactionEntity(3, TransactionType.IBAN_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "trimis bani prieten", 200));
        transactions.add(new TransactionEntity(4, TransactionType.WALLET_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "cumparaturi haine", 300));
        transactions.add(new TransactionEntity(1, TransactionType.WALLET_TO_IBAN, "RO01RNCB00000000123456789", "1801203250054", "ion popescu", "cumparaturi mancare", 200));

        for (TransactionEntity transaction : transactions) {
            template.insert(transaction);
        }
    }


}
