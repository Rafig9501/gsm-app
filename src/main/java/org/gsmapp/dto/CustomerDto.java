package org.gsmapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;
import org.gsmapp.util.validation_groups.CustomerCreate;
import org.gsmapp.util.validation_groups.TransactionOperations;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Builder
@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {

    @JsonProperty(value = "id")
    private UUID id;

    @NotBlank(message = "Customer name cannot be null or empty", groups = {CustomerCreate.class})
    @JsonProperty(value = "name")
    private String name;

    @NotNull(message = "Customer surname cannot be null or empty", groups = {CustomerCreate.class})
    @JsonProperty(value = "surname")
    private String surname;

    @NotNull(message = "Birth Date cannot be null", groups = {CustomerCreate.class})
    @JsonProperty(value = "birthDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @NotBlank(message = "GSM Number cannot be null or empty", groups = {CustomerCreate.class})
    @JsonProperty(value = "GSMNumber")
    private String GSMNumber;

    @JsonProperty(value = "balance")
    private String balance;

    @NotNull(message = "Transaction cannot be null", groups = TransactionOperations.class)
    @JsonProperty(value = "transaction")
    private TransactionDto transactionDto;

    @NotNull(message = "User Id cannot be null or empty", groups = {CustomerCreate.class})
    @JsonProperty(value = "userId")
    private UUID userId;
}