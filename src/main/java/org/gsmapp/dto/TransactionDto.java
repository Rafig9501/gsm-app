package org.gsmapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.gsmapp.util.validation_groups.CustomerCreate;
import org.gsmapp.util.validation_groups.TransactionOperations;

import java.math.BigDecimal;
import java.util.UUID;


@Builder
@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private UUID id;

    @NotNull(message = "Transaction amount cannot be null", groups = {TransactionOperations.class})
    private BigDecimal amount;
}