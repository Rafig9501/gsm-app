package org.gsmapp.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gsmapp.util.SecurityConstants;
import org.gsmapp.dto.TokenDto;
import org.gsmapp.dto.UserDto;
import org.gsmapp.entity.UserEntity;
import org.gsmapp.exception.AuthenticationException;
import org.gsmapp.repository.UserRepository;
import org.gsmapp.security.token.JwtTokenService;
import org.gsmapp.service.SecurityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.gsmapp.util.ExceptionMessage.*;
import static org.gsmapp.util.Constants.AUTHORIZATION;
import static org.gsmapp.util.Constants.BEARER;


@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityServiceImpl implements SecurityService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityConstants securityConstants;

    @Override
    public TokenDto createAuthenticationTokenPair(String username, String password) {
        try {
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(AUTHENTICATION_EXCEPTION, e);
        }
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(TOKEN_NOT_FOUND));
        UserDto userDto = UserDto.builder()
                .username(username)
                .id(userEntity.getId())
                .build();
        String accessToken = jwtTokenService.generateAccessToken(userDto);
        String refreshToken = jwtTokenService.generateRefreshToken(userDto);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessExpirationInSeconds(securityConstants.getAccessExpiration())
                .refreshExpirationInSeconds(securityConstants.getRefreshExpiration())
                .build();
    }

    @Override
    public TokenDto refreshToken(HttpServletRequest request) {
        String token = getAuthorizationToken(request)
                .orElseThrow(() -> new AuthenticationException(TOKEN_NOT_FOUND_IN_REQUEST));
        if (jwtTokenService.tokenIsValid(token) && jwtTokenService.tokenCanBeRefreshed(token)) {
            final UserEntity userEntity = jwtTokenService.getOwner(token)
                    .map(x -> userRepository.findByUsername(x.getUsername())
                            .orElseThrow(() -> new AuthenticationException(TOKEN_NOT_FOUND)))
                    .orElseThrow(() -> new AuthenticationException(TOKEN_NOT_FOUND));
            UserDto userDto = UserDto.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .build();
            return jwtTokenService.refreshToken(token, userDto);
        }
        throw new AuthenticationException(TOKEN_CANNOT_BE_REFRESHED);
    }

    private Optional<String> getAuthorizationToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER))
                .map(authHeader -> authHeader.substring(7));
    }
}
