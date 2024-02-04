package org.gsmapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "user_", uniqueConstraints = {@UniqueConstraint(columnNames = "user_name_")})
public class UserEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id_")
    private UUID id;

    @Column(name = "user_name_")
    private String username;

    @Column(name = "password_")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CustomerEntity> customers;
}
