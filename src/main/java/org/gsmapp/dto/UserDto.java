package org.gsmapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Builder
@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @JsonProperty(value = "id")
    private UUID id;

    @NotBlank(message = "Username cannot be null or empty")
    @Size(min = 5, max = 12, message = "Username must be between 5 and 12 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username cannot contain special characters")
    @JsonProperty(value = "username")
    private String username;

    @NotEmpty(message = "Password cannot be null or empty")
    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 8, max = 30, message = "Password length must be between 8 and 30 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]+$",
            message = "Password must contain at least 1 special character and 1 number")
    private String password;
}