package org.gsmapp.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class ExpirationDateCalculator {

    private final Clock clock;

    private final Integer accessTokenTtlInHours;
    private final Integer refreshTokenTtlInDays;
    private final Integer refreshTokenTtlOffsetInMinutes;

    public ExpirationDateCalculator(Clock clock,
                                    @Value("${security.jwt_access_token_expiration}") Integer verificationAccessTokenTtl,
                                    @Value("${security.jwt_refresh_token_expiration}") Integer verificationRefreshTokenTtl) {
        this.clock = clock;
        this.accessTokenTtlInHours = verificationAccessTokenTtl;
        this.refreshTokenTtlInDays = verificationRefreshTokenTtl;
        this.refreshTokenTtlOffsetInMinutes = 0;
    }

    public Instant calculateAccessTokenExpirationDate(Instant tokenCreatedDate) {
        ZoneId zoneId = clock.getZone();

        ZonedDateTime nowTime = tokenCreatedDate.atZone(zoneId);
        ZonedDateTime expirationTime = nowTime.plusHours(accessTokenTtlInHours);

        return expirationTime.toInstant();
    }

    public Instant calculateRefreshTokenExpirationDate(Instant tokenCreatedDate) {
        ZoneId zoneId = clock.getZone();

        ZonedDateTime expirationTime = LocalDateTime.ofInstant(tokenCreatedDate, zoneId)
                .plusDays(refreshTokenTtlInDays)
                .plusMinutes(refreshTokenTtlOffsetInMinutes)
                .atZone(zoneId);

        return expirationTime.toInstant();
    }
}