package org.gsmapp.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecurityConstants {

    @Value("${security.jwt_secret}")
    public String secret;

    @Value("${security.jwt_token_issuer}")
    public String issuer;

    @Value("${security.jwt_access_token_expiration}")
    private Long accessExpiration;

    @Value("${security.jwt_refresh_token_expiration}")
    private Long refreshExpiration;
}