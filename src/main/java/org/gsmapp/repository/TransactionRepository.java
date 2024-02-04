package org.gsmapp.repository;

import org.gsmapp.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query(value = "SELECT * FROM transaction_ " +
            "WHERE customer_id_ = :customerId " +
            "AND type_ = CAST(:transactionType AS TRANSACTION_TYPE) " +
            "ORDER BY date_ DESC LIMIT 1", nativeQuery = true)
    Optional<TransactionEntity> findByCustomerIdAndTransactionType(
            @Param("customerId") UUID customerId,
            @Param("transactionType") String transactionType
    );
}