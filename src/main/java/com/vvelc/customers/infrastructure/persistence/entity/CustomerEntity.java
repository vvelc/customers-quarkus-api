package com.vvelc.customers.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customers_email", columnList = "email"),
                @Index(name = "idx_customers_country", columnList = "country")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String secondName;

    @Column(nullable = false, length = 50)
    private String firstLastName;

    @Column(length = 50)
    private String secondLastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(length = 50)
    private String demonym;

    // IDEA: We should consider adding createdAt and updatedAt fields to track when the customer was created and last updated.
    // Didn't add them because the requirements were clear about which fields were needed for this technical test.
}