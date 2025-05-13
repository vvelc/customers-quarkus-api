package com.vvelc.customers.application.service;

import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.application.model.PageRequest;
import com.vvelc.customers.application.model.PageResponse;
import com.vvelc.customers.application.port.outbound.CountryValidationPort;
import com.vvelc.customers.domain.exception.*;
import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
// TODO: @RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryValidationPort countryValidationPort;

    @Inject
    public CustomerService(CustomerRepository customerRepository, CountryValidationPort countryValidationPort) {
        this.customerRepository = customerRepository;
        this.countryValidationPort = countryValidationPort;
    }
    
    private boolean isEmailAlreadyRegistered(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Transactional
    @Counted(value = "customers_created", description = "Total de clientes creados")
    @Timed(value = "customer_creation_time", description = "Tiempo en registrar un cliente")
    public Long createCustomer(
            String firstName, String secondName,
            String firstLastName, String secondLastName,
            String email, String address,
            String phone, String country
    ) throws BadRequestException, CountryNotFoundException, CountryServiceException {
        Log.infof("Creating new customer with name: %s %s %s %s", firstName, secondName, firstLastName, secondLastName);

        Log.info("Validating country: " + country);
        CountryInfo countryInfo = countryValidationPort.findByIsoCode(country);

        Log.infof("Country validated successfully: %s %s %s", countryInfo.name(), countryInfo.isoCode(), countryInfo.demonym());
        final String countryDemonym = countryInfo.demonym();

        Log.info("Checking if email is already registered: " + email);
        if (isEmailAlreadyRegistered(email)) {
            Log.error("Email already registered: " + email);
            throw new BadRequestException("Email already registered: " + email);
        }

        Customer customer = new Customer(
                null,
                firstName,
                secondName,
                firstLastName,
                secondLastName,
                email,
                address,
                phone,
                country,
                countryDemonym
        );

        customerRepository.save(customer);

        Log.info("Customer created successfully with ID: " + customer.getId());

        return customer.getId();
    }

    @Timed(value = "customer.fetch.time", description = "Tiempo en buscar un cliente")
    public Customer getCustomerById(Long customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    Log.info("Customer found: " + customer.getId());
                    return customer;
                })
                .orElseThrow(() -> {
                    Log.error("Customer not found: " + customerId);
                    return new CustomerNotFoundException("Customer not found: " + customerId);
                });
    }

    @Timed(value = "customer.fetch.by.country.time", description = "Tiempo en buscar clientes por país")
    public PageResponse<Customer> getCustomersByCountry(String country, PageRequest pageRequest) {
        Log.info("Fetching customers by country: " + country);

        List<Customer> customers = customerRepository.findByCountry(country, pageRequest.page(), pageRequest.size());
        Long total = customerRepository.countByCountry(country);

        return new PageResponse<>(
                customers,
                pageRequest.page(),
                pageRequest.size(),
                total
        );
    }

    @Timed(value = "customer.fetch.all.time", description = "Tiempo en obtener todos los clientes")
    public PageResponse<Customer> getAllCustomers(PageRequest pageRequest) {
        Log.info("Fetching all customers");

        List<Customer> customers = customerRepository.findAll(pageRequest.page(), pageRequest.size());
        Long total = customerRepository.count();

        return new PageResponse<>(
                customers,
                pageRequest.page(),
                pageRequest.size(),
                total
        );
    }

    @Transactional
    @Counted(value = "customers.updated", description = "Total de clientes actualizados")
    @Timed(value = "customer.update.time", description = "Tiempo en actualizar un cliente")
    public Customer updateCustomer(
            Long id, String email,
            String address, String phone,
            String country
    ) throws IllegalStateException {
        Log.info("Attempting to update customer with ID: " + id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    Log.error("Customer not found for update: " + id);
                    return new CustomerNotFoundException("Customer not found for update: " + id);
                });
        if (country != null && !country.isBlank()) {
            Log.info("Validating country: " + country);
            CountryInfo countryInfo = countryValidationPort.findByIsoCode(country);

            Log.infof("Country validated successfully: %s %s %s", countryInfo.name(), countryInfo.isoCode(), countryInfo.demonym());
            final String countryDemonym = countryInfo.demonym();

            existingCustomer.setCountry(country);
            existingCustomer.setDemonym(countryDemonym);
        }

        if (isEmailAlreadyRegistered(email) && !existingCustomer.getEmail().equals(email)) {
            Log.error("Email already registered: " + email);
            throw new BadRequestException("Email already registered: " + email);
        }

        if (email != null && !email.isBlank()) existingCustomer.setEmail(email);
        if (address != null && !address.isBlank()) existingCustomer.setAddress(address);
        if (phone != null && !phone.isBlank()) existingCustomer.setPhone(phone);

        return customerRepository.update(existingCustomer)
                .map(updatedCustomer -> {
                    Log.info("Customer updated successfully with ID: " + updatedCustomer.getId());
                    return updatedCustomer;
                })
                .orElseThrow(() -> {
                    Log.error("Failed to update customer with ID: " + id);
                    return new IllegalStateException("Error updating customer with ID: " + id);
                });
    }

    @Transactional
    @Counted(value = "customers.deleted", description = "Total de clientes eliminados")
    @Timed(value = "customer.deletion.time", description = "Tiempo en eliminar un cliente")
    public boolean deleteCustomer(Long id) throws CustomerNotFoundException {
        Log.info("Attempting to delete customer with ID: " + id);

        boolean isDeleted = customerRepository.deleteById(id);

        if (!isDeleted) {
            Log.warn("Customer not found for deletion with ID: " + id);
            throw new CustomerNotFoundException("Customer not found for deletion: " + id);
        }

        Log.info("Customer successfully deleted with ID: " + id);
        return true;
    }
}