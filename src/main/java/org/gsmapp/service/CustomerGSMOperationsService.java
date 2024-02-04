package org.gsmapp.service;

import org.gsmapp.dto.CustomerDto;
import org.springframework.stereotype.Service;

@Service
public interface CustomerGSMOperationsService {

    CustomerDto topUpBalance(CustomerDto dto);

    CustomerDto purchase(CustomerDto dto);

    CustomerDto refund(CustomerDto dto);

    CustomerDto createCustomer(CustomerDto dto);
}
