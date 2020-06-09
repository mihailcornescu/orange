package com.orange.microservices.transaction.store.services;

import com.orange.microservices.transaction.store.persistence.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.orange.api.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction entityToApi(TransactionEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    TransactionEntity apiToEntity(Transaction api);
}
