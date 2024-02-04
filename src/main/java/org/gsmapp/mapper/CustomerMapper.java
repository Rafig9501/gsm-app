package org.gsmapp.mapper;

import org.gsmapp.dto.CustomerDto;
import org.gsmapp.entity.CustomerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "balance", expression = "java(java.math.BigDecimal.ZERO)")
    CustomerEntity customerDtoToCustomerEntity(CustomerDto dto);

    CustomerDto customerEntityToCustomerDto(CustomerEntity dto);
}
