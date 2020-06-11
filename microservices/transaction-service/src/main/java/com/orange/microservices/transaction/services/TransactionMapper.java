package com.orange.microservices.transaction.services;

import com.orange.api.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.orange.microservices.transaction.persistence.TransactionEntity;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mappings({
    })
    Transaction entityToApi(TransactionEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    TransactionEntity apiToEntity(Transaction api);
}
