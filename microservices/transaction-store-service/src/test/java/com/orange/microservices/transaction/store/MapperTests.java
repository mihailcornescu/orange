package com.orange.microservices.transaction.store;

import org.junit.Test;
import org.mapstruct.factory.Mappers;
import com.orange.api.model.Transaction;
import com.orange.microservices.transaction.store.persistence.TransactionEntity;
import com.orange.microservices.transaction.store.services.TransactionMapper;

import static org.junit.Assert.*;

public class MapperTests {

    private TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        Transaction api = new Transaction(1, "n", 1);

        TransactionEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getTransactionId(), entity.getTransactionId());
        assertEquals(api.getName(), entity.getName());
        assertEquals(api.getAmount(), entity.getAmount());

        Transaction api2 = mapper.entityToApi(entity);

        assertEquals(api.getTransactionId(), api2.getTransactionId());
        assertEquals(api.getName(),      api2.getName());
        assertEquals(api.getAmount(),    api2.getAmount());
    }
}
