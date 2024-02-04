package org.gsmapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.gsmapp.dto.CustomerDto;
import org.gsmapp.entity.CustomerEntity;
import org.gsmapp.entity.TransactionEntity;
import org.gsmapp.entity.TransactionType;
import org.gsmapp.entity.UserEntity;
import org.gsmapp.exception.AuthorizationException;
import org.gsmapp.exception.EntityNotFoundException;
import org.gsmapp.mapper.CustomerMapper;
import org.gsmapp.mapper.TransactionMapper;
import org.gsmapp.repository.CustomerRepository;
import org.gsmapp.repository.TransactionRepository;
import org.gsmapp.repository.UserRepository;
import org.gsmapp.service.CustomerGSMOperationsService;
import org.gsmapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.gsmapp.entity.TransactionType.*;
import static org.gsmapp.util.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class CustomerGSMOperationsServiceImpl implements CustomerGSMOperationsService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserService userService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public CustomerDto topUpBalance(CustomerDto customerDto) {
        return customerMapper.customerEntityToCustomerDto(customerRepository.save(operate(customerDto, TOP_UP)));
    }

    @SneakyThrows
    @Override
    public CustomerDto purchase(CustomerDto customerDto) {
        return customerMapper.customerEntityToCustomerDto(customerRepository.save(operate(customerDto, PURCHASE)));
    }

    @SneakyThrows
    @Override
    public CustomerDto refund(CustomerDto customerDto) {
        return customerMapper.customerEntityToCustomerDto(customerRepository.save(operate(customerDto, REFUND)));
    }

    @SneakyThrows
    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        checkAuthorizedUser(dto.getUserId());
        CustomerEntity customerEntity = customerMapper.customerDtoToCustomerEntity(dto);
        customerEntity.setUser(findAuthenticatedUser());
        return customerMapper
                .customerEntityToCustomerDto(customerRepository
                        .save(customerEntity));
    }

    @SneakyThrows
    private CustomerEntity operate(CustomerDto customerDto, TransactionType type) {
        CustomerEntity customerEntity = customerRepository.findById(customerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_NOT_FOUND));
        checkAuthorizedUser(customerEntity.getUser().getId());
        BigDecimal balance = customerEntity.getBalance();
        BigDecimal amount = customerDto.getTransactionDto().getAmount();
        switch (type) {
            case TOP_UP -> {
                customerEntity.setBalance(balance.add(amount));
                saveUpdatedTransactions(customerDto, customerEntity, type);
                return customerEntity;
            }
            case PURCHASE -> {
                customerEntity.setBalance(balance.subtract(amount));
                saveUpdatedTransactions(customerDto, customerEntity, type);
                return customerEntity;
            }
            case REFUND -> {
                TransactionEntity transactionEntity = transactionRepository
                        .findByCustomerIdAndTransactionType(customerEntity.getId(), PURCHASE.toString())
                        .orElseThrow(() -> new EntityNotFoundException(TRANSACTION_NOT_FOUND));
                customerEntity.setBalance(customerEntity.getBalance().subtract(customerDto.getTransactionDto().getAmount()));
                transactionRepository.save(TransactionEntity.builder()
                        .originalTransaction(transactionEntity)
                        .customer(customerEntity)
                        .date(LocalDateTime.now())
                        .amount(customerDto.getTransactionDto().getAmount())
                        .transactionType(REFUND)
                        .build());
                return customerEntity;
            }
            default -> throw new IllegalArgumentException(INVALID_TRANSACTION_TYPE + type);
        }
    }

    @SneakyThrows
    private void checkAuthorizedUser(UUID userId) {
        UserEntity user = userService.getUserById(findAuthenticatedUser().getId());
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!user.getUsername().equals(authenticatedUsername) || !user.getId().equals(userId)) {
            throw new AuthorizationException(AUTHORIZATION_EXCEPTION);
        }
    }

    private void saveUpdatedTransactions(CustomerDto customerDto, CustomerEntity customerEntity, TransactionType type) {
        TransactionEntity transactionEntity = transactionMapper.transactionDtoToTransactionEntity(customerDto.getTransactionDto());
        transactionEntity.setTransactionType(type);
        transactionEntity.setCustomer(customerEntity);
        customerEntity.getTransactions().add(transactionEntity);
        transactionRepository.save(transactionEntity);
    }

    private UserEntity findAuthenticatedUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }
}