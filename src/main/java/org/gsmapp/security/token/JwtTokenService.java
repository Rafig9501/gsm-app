package org.gsmapp.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.gsmapp.util.SecurityConstants;
import org.gsmapp.dto.UserDto;
import org.gsmapp.dto.TokenDto;
import org.gsmapp.entity.TokenEntity;
import org.gsmapp.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;


@Service
@AllArgsConstructor
public class JwtTokenService {

    private static final String CREATED_AT = "createdAt";
    private static final String ISSUED_AT = "issuedAt";
    private static final String EXPIRATION_DATE = "expirationDate";
    private static final String REFRESH_EXPIRATION_DATE = "refreshExpirationDate";
    private static final String AUTHORITIES_CLAIM = "AUTHORITIES_CLAIM";
    private static final String USER_ID = "userId";

    private final Clock clock;
    private final SecurityConstants JWT_CONFIG;
    private final ExpirationDateCalculator expirationDateCalculator;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<UserDto> getOwner(String token) {
        return Optional.of(
                UserDto
                        .builder()
                        .username(getAllClaimsFromToken(token).getSubject())
                        .build()
        );
    }

    public String generateAccessToken(UserDto UserDto) {
        Map<String, Object> claims = doGenerateClaims(UserDto);
        setAccessTokenTimeline(claims);
        return doGenerateToken(claims, UserDto.getUsername());
    }

    public String generateRefreshToken(UserDto UserDto) {
        UUID userId = UserDto.getId();
        Map<String, Object> claims = setRefreshTokenTimeline(doGenerateClaims(UserDto));
        String refreshToken = doGenerateToken(claims, UserDto.getUsername());
        Instant refreshTokenIssuedAtDate = Instant.ofEpochMilli(getIssuedAtDateFromToken(refreshToken));
        persistRefreshToken(userId, refreshTokenIssuedAtDate);
        return refreshToken;
    }

    @Transactional
    public TokenDto refreshToken(String oldToken, UserDto UserDto) {
        Claims claims = getAllClaimsFromToken(oldToken);
        String subject = claims.getSubject();
        UUID userId = UserDto.getId();

        Map<String, Object> claimsNew = setRefreshTokenTimeline(claims);
        String refreshToken = doGenerateToken(claims, subject);

        claimsNew.put(AUTHORITIES_CLAIM, String.join(",", new ArrayList<>()));
        setAccessTokenTimeline(claimsNew);
        String accessToken = doGenerateToken(claimsNew, subject);

        Instant newTokenIssuedAtDate = Instant.ofEpochMilli(getIssuedAtDateFromToken(refreshToken));
        Instant oldTokenIssuedAtDate = Instant.ofEpochMilli(getIssuedAtDateFromToken(oldToken));

        persistRefreshToken(userId, newTokenIssuedAtDate);
        deleteOldRefreshToken(userId, oldTokenIssuedAtDate);

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public boolean tokenCanBeRefreshed(String token) {
        final Instant expirationDate = Instant.ofEpochMilli(getRefreshExpirationDate(token));
        final Instant now = getInstant();

        UUID userId = UUID.fromString(getUserIdFromToken(token));
        Instant tokenIssuedAtDate = Instant.ofEpochMilli(getIssuedAtDateFromToken(token));

        return (now.isBefore(expirationDate) && existsByUserIdAndIssuedAt(userId, tokenIssuedAtDate));
    }

    private boolean existsByUserIdAndIssuedAt(UUID userId, Instant issuedAt) {
        return refreshTokenRepository.existsByUserIdAndIssuedAt(userId, issuedAt);
    }

    public boolean tokenIsValid(String token) {
        return !isTokenExpired(token);
    }

    private Long getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, (claims) -> claims.get(ISSUED_AT, Long.class));
    }

    private Long getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, (claims) -> claims.get(EXPIRATION_DATE, Long.class));
    }

    private Long getRefreshExpirationDate(String token) {
        return getClaimFromToken(token, (claims) -> claims.get(REFRESH_EXPIRATION_DATE, Long.class));
    }

    private String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get(USER_ID, String.class));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(JWT_CONFIG.getSecret().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return Instant.ofEpochMilli(getExpirationDateFromToken(token)).isBefore(getInstant());
    }

    private Map<String, Object> doGenerateClaims(UserDto UserDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, UserDto.getId());
        return claims;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_CONFIG.getSecret().getBytes());
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(JWT_CONFIG.getIssuer())
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    private void setAccessTokenTimeline(Map<String, Object> claims) {
        Instant createdDate = getInstant();
        Instant expirationDate = expirationDateCalculator.calculateAccessTokenExpirationDate(createdDate);

        claims.put(CREATED_AT, createdDate.toEpochMilli());
        claims.put(EXPIRATION_DATE, expirationDate.toEpochMilli());
        claims.put(REFRESH_EXPIRATION_DATE, createdDate.toEpochMilli());
    }

    private Map<String, Object> setRefreshTokenTimeline(Map<String, Object> claimsOld) {
        Instant createdDate = getInstant();
        Instant refreshExpirationDate = expirationDateCalculator.calculateRefreshTokenExpirationDate(createdDate);

        Map<String, Object> newClaims = new HashMap<>(claimsOld);

        newClaims.put(ISSUED_AT, createdDate.toEpochMilli());
        newClaims.put(EXPIRATION_DATE, refreshExpirationDate.toEpochMilli());
        newClaims.put(REFRESH_EXPIRATION_DATE, refreshExpirationDate.toEpochMilli());
        return newClaims;
    }

    private Instant getInstant() {
        return clock.instant();
    }

    private void persistRefreshToken(UUID userId, Instant issuedAtDate) {
        TokenEntity tokenEntity = TokenEntity.builder()
                .userId(userId)
                .issuedAt(issuedAtDate)
                .build();
        refreshTokenRepository.save(tokenEntity);
    }

    private void deleteOldRefreshToken(UUID userId, Instant issuedAtDate) {
        if (refreshTokenRepository.existsByUserIdAndIssuedAt(userId, issuedAtDate))
            refreshTokenRepository.deleteByUserIdAndIssuedAt(userId, issuedAtDate);
    }
}