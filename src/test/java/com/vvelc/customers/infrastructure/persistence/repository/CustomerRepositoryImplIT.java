package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CustomerRepositoryImpl Integration Test")
class CustomerRepositoryImplIT {

    @Inject
    CustomerRepository repository;

    Long savedId1;
    Long savedId2;

    @BeforeEach
    @Transactional
    void setup() {
        Customer c1 = new Customer(null, "Alice", null, "Smith", null, "alice@test.com", "Main St", "123", "US", "American");
        Customer c2 = new Customer(null, "Bob", null, "Brown", null, "bob@test.com", "Second St", "456", "US", "American");

        savedId1 = repository.save(c1).getId();
        savedId2 = repository.save(c2).getId();
    }

    @AfterEach
    @Transactional
    void clean() {
        repository.deleteById(savedId1);
        repository.deleteById(savedId2);
    }

    @Test
    @DisplayName("Should save and retrieve customer by ID")
    void should_save_and_find_by_id() {
        Optional<Customer> found = repository.findById(savedId1);
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    @DisplayName("Should return empty when customer not found")
    void should_return_empty_if_not_found() {
        Optional<Customer> result = repository.findById(9999L);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find all customers with pagination")
    void should_find_all_customers() {
        List<Customer> customers = repository.findAll(0, 10);
        assertThat(customers).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should find customers by country")
    void should_find_by_country() {
        List<Customer> customers = repository.findByCountry("US", 0, 10);
        assertThat(customers).allMatch(c -> c.getCountry().equals("US"));
    }

    @Test
    @DisplayName("Should update customer fields")
    @Transactional
    void should_update_customer() {
        Customer updated = new Customer(savedId1, null, null, null, null, "alice.new@test.com", "New Addr", "999", "CA", "Canadian");
        Optional<Customer> result = repository.update(updated);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice.new@test.com");
        assertThat(result.get().getCountry()).isEqualTo("CA");
    }

    @Test
    @DisplayName("Should delete customer by ID")
    @Transactional
    void should_delete_customer_by_id() {
        boolean deleted = repository.deleteById(savedId2);
        assertThat(deleted).isTrue();

        Optional<Customer> result = repository.findById(savedId2);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should count all customers")
    void should_count_all() {
        Long count = repository.count();
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should count customers by country")
    void should_count_by_country() {
        Long count = repository.countByCountry("US");
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should detect existing email")
    void should_detect_existing_email() {
        boolean exists = repository.existsByEmail("alice@test.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should detect non-existing email")
    void should_detect_non_existing_email() {
        boolean exists = repository.existsByEmail("nonexistent@mail.com");
        assertThat(exists).isFalse();
    }
}
