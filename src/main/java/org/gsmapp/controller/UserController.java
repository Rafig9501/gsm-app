package org.gsmapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.gsmapp.dto.UserDto;
import org.gsmapp.dto.TokenDto;
import org.gsmapp.service.impl.SecurityServiceImpl;
import org.gsmapp.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.gsmapp.util.Constants.AUTHORIZATION;


@AllArgsConstructor
@RestController
@RequestMapping(path = "/user")
@Validated
public class UserController {

    private final SecurityServiceImpl securityServiceImpl;
    private final UserService userService;

    @Operation(description = "Sign in",
            responses = @ApiResponse(responseCode = "200")
    )
    @PostMapping(path = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> login(@RequestBody @Validated UserDto dto) {
        return ResponseEntity.ok(securityServiceImpl.createAuthenticationTokenPair(dto.getUsername(), dto.getPassword()));
    }

    @Operation(description = "Sign up",
            responses = @ApiResponse(responseCode = "200")
    )
    @PostMapping(path = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@RequestBody @Validated UserDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @Operation(description = "Get Refresh and Access tokens",
            responses = @ApiResponse(responseCode = "200")
    )
    @PostMapping(path = "/refresh-token", headers = AUTHORIZATION)
    public ResponseEntity<TokenDto> refreshTokens(HttpServletRequest request) {
        return ResponseEntity.ok(securityServiceImpl.refreshToken(request));
    }
}