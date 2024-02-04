package org.gsmapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDto {

    @JsonProperty(value = "accessToken")
    private String accessToken;

    @JsonProperty(value = "refreshToken")
    private String refreshToken;

    @JsonProperty(value = "accessExpirationInSeconds")
    private Long accessExpirationInSeconds;

    @JsonProperty(value = "refreshExpirationInSeconds")
    private Long refreshExpirationInSeconds;
}
