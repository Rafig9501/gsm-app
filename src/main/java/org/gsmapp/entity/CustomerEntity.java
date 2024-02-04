package org.gsmapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "customer_", uniqueConstraints = {@UniqueConstraint(columnNames = "gsm_number_")})
public class CustomerEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id_")
    private UUID id;

    @Column(name = "name_")
    private String name;

    @Column(name = "surname_")
    private String surname;

    @Column(name = "birthdate_")
    private LocalDate birthDate;

    @Column(name = "gsm_number_")
    private String GSMNumber;

    @Column(name = "balance_")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id_", referencedColumnName = "id_")
    private UserEntity user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;
}
