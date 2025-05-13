package com.vvelc.customers.application.service;

import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.application.model.PageRequest;
import com.vvelc.customers.application.model.PageResponse;
import com.vvelc.customers.domain.exception.*;
import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import com.vvelc.customers.application.port.outbound.CountryValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CountryValidationPort countryValidationPort;

    @InjectMocks
    CustomerService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createCustomer")
    class CreateCustomerTests {

        @Test
        void should_create_customer_successfully() {
            Customer input = new Customer(
                    null, "John", null, "Doe", null,
                    "john@mail.com", "123 Street #1", "8091231234", "US", null
            );

            CountryInfo countryInfo = new CountryInfo("US", "United States", "American");

            Customer saved = new Customer(
                    1L, "John", null, "Doe", null,
                    "john@mail.com", "123 Street #1", "8091231234", "US", "American"
            );

            when(customerRepository.existsByEmail(input.getEmail())).thenReturn(false);
            when(countryValidationPort.findByIsoCode("US")).thenReturn(countryInfo);
            when(customerRepository.save(any())).thenReturn(saved);

            Customer createdCustomer = service.createCustomer(input);

            assertThat(createdCustomer.getId()).isEqualTo(1L);
            assertThat(createdCustomer.getDemonym()).isEqualTo("American");
            assertThat(createdCustomer.getEmail()).isEqualTo("john@mail.com");

            assertThat(createdCustomer).usingRecursiveComparison().isEqualTo(saved);
        }

        @Test
        void should_throw_when_email_already_exists() {
            Customer customer = new Customer(null, "John", null, "Doe", null, "john@mail.com", null, null, "US", null);
            when(countryValidationPort.findByIsoCode("US")).thenReturn(new CountryInfo("US", "United States", "American"));
            when(customerRepository.existsByEmail("john@mail.com")).thenReturn(true);

            assertThatThrownBy(() -> service.createCustomer(customer))
                    .isInstanceOf(CustomerAlreadyExistsException.class);

            verify(customerRepository, never()).save(any());
        }

        @Test
        void should_throw_when_country_not_found() {
            Customer customer = new Customer(null, "John", null, "Doe", null, "john@mail.com", null, null, "ZZ", null);

            when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
            when(countryValidationPort.findByIsoCode("ZZ")).thenThrow(new CountryNotFoundException("ZZ"));

            assertThatThrownBy(() -> service.createCustomer(customer))
                    .isInstanceOf(CountryNotFoundException.class);
        }

        @Test
        void should_throw_when_country_service_fails_on_create() {
            Customer customer = new Customer(null, "John", null, "Doe", null, "john@mail.com", null, null, "US", null);

            when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
            when(countryValidationPort.findByIsoCode("US")).thenThrow(new CountryServiceException("API down"));

            assertThatThrownBy(() -> service.createCustomer(customer))
                    .isInstanceOf(CountryServiceException.class)
                    .hasMessageContaining("API down");

            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getCustomer")
    class GetCustomerTests {

        @Test
        void should_get_customer_by_id() {
            Customer customer = new Customer(1L, "A", null, "B", null, "a@mail.com", null, null, "US", "American");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

            Customer result = service.getCustomerById(1L);
            assertThat(result).isEqualTo(customer);
        }

        @Test
        void should_throw_when_customer_not_found_by_id() {
            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getCustomerById(999L))
                    .isInstanceOf(CustomerNotFoundException.class);
        }

        @Test
        void should_get_all_customers() {
            when(customerRepository.findAll(0, 10)).thenReturn(List.of(new Customer()));

            PageRequest pageRequest = new PageRequest(0, 10);

            PageResponse<Customer> pageCustomers = service.getAllCustomers(pageRequest);
            assertThat(pageCustomers.getItems()).hasSize(1);
        }

        @Test
        void should_get_customers_by_country() {
            when(customerRepository.findByCountry("US", 0, 10)).thenReturn(List.of(new Customer()));

            PageRequest pageRequest = new PageRequest(0, 10);

            PageResponse<Customer> pageCustomers = service.getCustomersByCountry("US", pageRequest);
            assertThat(pageCustomers.getItems()).hasSize(1);
        }

        @Test
        void should_return_empty_list_when_no_customers_found() {
            when(customerRepository.findAll(0, 10)).thenReturn(List.of());

            PageRequest pageRequest = new PageRequest(0, 10);

            PageResponse<Customer> customers = service.getAllCustomers(pageRequest);

            assertThat(customers.getItems()).isEmpty();
        }

        @Test
        void should_return_empty_list_when_no_customers_found_by_country() {
            when(customerRepository.findByCountry("US", 0, 10)).thenReturn(List.of());

            PageRequest pageRequest = new PageRequest(0, 10);

            PageResponse<Customer> customers = service.getCustomersByCountry("US", pageRequest);

            assertThat(customers.getItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateCustomer")
    class UpdateCustomerTests {

        @Test
        void should_update_existing_customer() {
            Customer customer = new Customer(1L, "Jane", null, "Doe", null, "jane@mail.com", "123 Street #1", "8091231234", "US", "American");
            Customer updated = new Customer(1L, "Jane", null, "Doe", null, "janedoe@mail.com", "123 Street #1", "8091231234", "MX", "Mexican");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(countryValidationPort.findByIsoCode("MX")).thenReturn(new CountryInfo("MX", "Mexico", "Mexican"));
            when(customerRepository.update(argThat(c ->
                    c.getEmail().equals("janedoe@mail.com")
                            && c.getCountry().equals("MX")
                            && c.getDemonym().equals("Mexican")
            ))).thenReturn(Optional.of(updated));
            Customer result = service.updateCustomer(1L, updated);
            assertThat(result.getDemonym()).isEqualTo("Mexican");
        }

        @Test
        void should_update_customer_with_same_email_without_conflict() {
            Customer existing = new Customer(1L, "Jane", null, "Doe", null, "jane@mail.com", null, null, "US", null);
            Customer update = new Customer(1L, "Jane", null, "Doe", null, "jane@mail.com", null, null, "US", null);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(customerRepository.existsByEmail("jane@mail.com")).thenReturn(true);
            when(customerRepository.update(update)).thenReturn(Optional.of(update));
            when(countryValidationPort.findByIsoCode("US")).thenReturn(new CountryInfo("US", "USA", "American"));

            Customer result = service.updateCustomer(1L, update);

            assertThat(result.getEmail()).isEqualTo("jane@mail.com");
        }

        @Test
        void should_update_customer_partially() {
            Customer existing = new Customer(1L, "Jane", "Mary", "Doe", "Smith", "jane@mail.com", "Old Address", "123", "US", "American");
            Customer partialUpdate = new Customer(1L, null, null, null, null, null, "New Address", "456", null, null);
            Customer updatedCustomer = new Customer(1L, "Jane", "Mary", "Joe", "Smith", "jane@gmail.com", "New Address", "456", "US", "American");

            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(countryValidationPort.findByIsoCode("US")).thenReturn(new CountryInfo("US", "USA", "American"));
            when(customerRepository.update(argThat(c ->
                    c.getAddress().equals("New Address")
                            && c.getPhone().equals("456")
            ))).thenReturn(Optional.of(updatedCustomer));

            Customer result = service.updateCustomer(1L, partialUpdate);

            assertThat(result.getAddress()).isEqualTo("New Address");
            assertThat(result.getPhone()).isEqualTo("456");
        }

        // Should not update id or name (firstName, secondName, firstLastName, secondLastName)
        @Test
        void should_not_update_customer_name() {
            Customer customer = new Customer(1L, "Jane", "Mary", "Doe", "Smith", "jane@mail.com", "123 Street #1", "8091231234", "US", "American");
            Customer updated = new Customer(1L, "John", "Marc", "Dan", "Houston", null, null, null, null, null);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(customerRepository.update(argThat(c ->
                    c.getFirstName().equals("Jane")
                            && c.getSecondName().equals("Mary")
                            && c.getFirstLastName().equals("Doe")
                            && c.getSecondLastName().equals("Smith")
            ))).thenReturn(Optional.of(customer));

            Customer result = service.updateCustomer(1L, updated);

            assertThat(result.getFirstName()).isEqualTo("Jane");
            assertThat(result.getSecondName()).isEqualTo("Mary");
            assertThat(result.getFirstLastName()).isEqualTo("Doe");
            assertThat(result.getSecondLastName()).isEqualTo("Smith");
        }

        @Test
        void should_throw_when_updating_nonexistent_customer() {
            Customer customer = new Customer(99L, "X", null, "Y", null, "x@mail.com", null, null, "US", null);
            when(countryValidationPort.findByIsoCode("US")).thenReturn(new CountryInfo("US", "USA", "American"));
            when(customerRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateCustomer(99L, customer))
                    .isInstanceOf(CustomerNotFoundException.class);
        }

        @Test
        void should_throw_when_updating_with_existing_email() {
            Customer existing = new Customer(1L, "Jane", null, "Doe", null, "jane@mail.com", null, null, "US", null);
            Customer updated = new Customer(1L, "Jane", null, "Doe", null, "conflict@mail.com", null, null, "US", null);

            when(countryValidationPort.findByIsoCode("US")).thenReturn(new CountryInfo("US", "USA", "American"));
            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(customerRepository.existsByEmail("conflict@mail.com")).thenReturn(true);

            assertThatThrownBy(() -> service.updateCustomer(1L, updated))
                    .isInstanceOf(CustomerAlreadyExistsException.class);
        }

        @Test
        void should_throw_when_country_service_fails_on_update() {
            Customer customer = new Customer(1L, "Jane", null, "Doe", null, "jane@mail.com", null, null, "US", null);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(countryValidationPort.findByIsoCode("US")).thenThrow(new CountryServiceException("Timeout"));

            assertThatThrownBy(() -> service.updateCustomer(1L, customer))
                    .isInstanceOf(CountryServiceException.class)
                    .hasMessageContaining("Timeout");
        }
    }

    @Nested
    @DisplayName("deleteCustomer")
    class DeleteCustomerTests {
        @Test
        void should_delete_customer_by_id() {
            when(customerRepository.deleteById(1L)).thenReturn(true);
            boolean deleted = service.deleteCustomer(1L);
            assertThat(deleted).isTrue();
        }

        @Test
        void should_throw_when_deleting_nonexistent_customer() {
            when(customerRepository.deleteById(404L)).thenReturn(false);
            assertThatThrownBy(() -> service.deleteCustomer(404L))
                    .isInstanceOf(CustomerNotFoundException.class);
        }
    }
}
