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
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customer_email", columnNames = "email")
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

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "second_name", length = 50)
    private String secondName;

    @Column(name = "first_last_name", nullable = false, length = 50)
    private String firstLastName;

    @Column(name = "second_last_name", length = 50)
    private String secondLastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "demonym", nullable = false, length = 50)
    private String demonym;

    // IDEA: We should consider adding createdAt and updatedAt fields to track when the customer was created and last updated.
    // Didn't add them because the requirements were clear about which fields were needed for this technical test.
}