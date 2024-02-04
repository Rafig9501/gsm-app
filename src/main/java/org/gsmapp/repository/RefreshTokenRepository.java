package org.gsmapp.repository;

import org.gsmapp.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<TokenEntity, UUID> {

    void deleteByUserIdAndIssuedAt(UUID userId, Instant issuedAtDate);

    boolean existsByUserIdAndIssuedAt(UUID userId, Instant issuedAtDate);
}
