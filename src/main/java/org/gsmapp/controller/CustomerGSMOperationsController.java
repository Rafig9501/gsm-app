package org.gsmapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.gsmapp.dto.CustomerDto;
import org.gsmapp.service.CustomerGSMOperationsService;
import org.gsmapp.util.validation_groups.CustomerCreate;
import org.gsmapp.util.validation_groups.TransactionOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.gsmapp.util.Constants.AUTHORIZATION;


@RestController
@RequestMapping(path = "/gsm-operations")
@RequiredArgsConstructor
@Validated
public class CustomerGSMOperationsController {

    private final CustomerGSMOperationsService customerGSMOperationsService;

    @Operation(description = "Create new Customer",
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping(path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = AUTHORIZATION)
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Validated({CustomerCreate.class}) CustomerDto dto) {
        return ResponseEntity.ok(customerGSMOperationsService.createCustomer(dto));
    }

    @Operation(description = "Top-up customer balance",
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping(path = "/top-up",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = AUTHORIZATION)
    public ResponseEntity<CustomerDto> topUpBalance(@RequestBody @Validated(TransactionOperations.class) CustomerDto dto) {
        return ResponseEntity.ok(customerGSMOperationsService.topUpBalance(dto));
    }

    @Operation(description = "Purchase from customer balance",
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping(path = "/purchase",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = AUTHORIZATION)
    public ResponseEntity<CustomerDto> purchase(@RequestBody @Validated(TransactionOperations.class) CustomerDto dto) {
        return ResponseEntity.ok(customerGSMOperationsService.purchase(dto));
    }

    @Operation(description = "Refund from customer balance",
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping(path = "/refund",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = AUTHORIZATION)
    public ResponseEntity<CustomerDto> refund(@RequestBody @Validated(TransactionOperations.class) CustomerDto dto) {
        return ResponseEntity.ok(customerGSMOperationsService.refund(dto));
    }
}
