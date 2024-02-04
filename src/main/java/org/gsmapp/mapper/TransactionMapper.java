package org.gsmapp.mapper;

import org.gsmapp.dto.TransactionDto;
import org.gsmapp.entity.TransactionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(target = "date", expression = "java(java.time.LocalDateTime.now())")
    TransactionEntity transactionDtoToTransactionEntity(TransactionDto dto);
}
