package org.gsmapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "transaction_")
public class TransactionEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id_")
    private UUID id;

    @Column(name = "amount_")
    private BigDecimal amount;

    @Column(name = "date_")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "original_transaction_id_", referencedColumnName = "id_")
    private TransactionEntity originalTransaction;

    @ManyToOne
    @JoinColumn(name = "customer_id_", referencedColumnName = "id_")
    private CustomerEntity customer;

    @Column(name = "type_")
    @Enumerated(STRING)
    private TransactionType transactionType;
}
