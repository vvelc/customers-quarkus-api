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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Servicio que orquesta la lógica de negocio para la gestión de Clientes.
 * Encapsula llamadas a repositorio y validación externa de país.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryValidationPort countryValidationPort;

    private boolean isEmailAlreadyRegistered(String email) {
        return customerRepository.existsByEmail(email);
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param customer DTO con datos de entrada (firstName, email, country…)
     * @return DTO de respuesta con ID, datos persistidos y Location calculada
     * @throws CustomerAlreadyExistsException   si ya hay un cliente con ese email
     * @throws CountryNotFoundException si el código de país no existe
     * @throws CountryServiceException si hay un error al consultar el servicio de validación de países
     */
    @Transactional
    @Counted(value = "customers_created", description = "Total de clientes creados")
    @Timed(value = "customer_creation_time", description = "Tiempo en registrar un cliente")
    public Customer createCustomer(Customer customer) throws CustomerAlreadyExistsException, CountryNotFoundException, CountryServiceException {
        Log.infof("Creating new customer with name: %s %s %s %s",
                customer.getFirstName(), customer.getSecondName(), customer.getFirstLastName(), customer.getSecondLastName());

        String country = customer.getCountry();
        Log.infof("Validating country: %s", country);
        CountryInfo countryInfo = countryValidationPort.findByIsoCode(country);

        Log.infof("Country validated successfully: %s %s %s", countryInfo.name(), countryInfo.isoCode(), countryInfo.demonym());
        final String countryDemonym = countryInfo.demonym();
        customer.setDemonym(countryDemonym);

        String email = customer.getEmail();
        Log.infof("Checking if email is already registered: %s", email);
        if (isEmailAlreadyRegistered(email)) {
            Log.errorf("Email already registered: %s", email);
            throw new CustomerAlreadyExistsException("Email already registered: " + email);
        }
        Log.infof("Email is available: %s", email);

        Log.infof("Saving customer to database: %s", customer);
        Customer createdCustomer = customerRepository.save(customer);
        Log.info("Customer created successfully with ID: " + createdCustomer.getId());

        return createdCustomer;
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param customerId ID del cliente a buscar
     * @return Objeto Customer con los datos del cliente
     * @throws CustomerNotFoundException si no se encuentra el cliente
     */
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

    /**
     * Busca clientes por país.
     *
     * @param country     Código del país
     * @param pageRequest Objeto con información de paginación
     * @return Lista de clientes en el país especificado
     */
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

    /**
     * Busca todos los clientes.
     *
     * @param pageRequest Objeto con información de paginación
     * @return Lista de todos los clientes
     */
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

    /**
     * Actualiza un cliente existente.
     *
     * @param id       ID del cliente a actualizar
     * @param customer Objeto Customer con los nuevos datos del cliente
     * @return Objeto Customer con los datos actualizados
     * @throws CustomerNotFoundException si no se encuentra el cliente
     * @throws CustomerAlreadyExistsException si el email ya está registrado
     * @throws IllegalStateException si ocurre un error al actualizar el cliente
     */
    @Transactional
    @Counted(value = "customers.updated", description = "Total de clientes actualizados")
    @Timed(value = "customer.update.time", description = "Tiempo en actualizar un cliente")
    public Customer updateCustomer(Long id, Customer customer)
            throws CustomerNotFoundException, CustomerAlreadyExistsException, IllegalStateException {

        Log.info("Attempting to update customer with ID: " + id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    Log.error("Customer not found for update: " + id);
                    return new CustomerNotFoundException("Customer not found for update: " + id);
                });

        String country = customer.getCountry();
        if (country != null && !country.isBlank()) {
            Log.info("Validating country: " + country);
            CountryInfo countryInfo = countryValidationPort.findByIsoCode(country);

            Log.infof("Country validated successfully: %s %s %s",
                    countryInfo.name(), countryInfo.isoCode(), countryInfo.demonym());
            final String countryDemonym = countryInfo.demonym();

            existingCustomer.setCountry(country);
            existingCustomer.setDemonym(countryDemonym);
        }

        String email = customer.getEmail();
        if (email != null && !email.isBlank()) {
            Log.infof("Validating email availability: %s", email);
            boolean isSameEmail = email.equals(existingCustomer.getEmail());
            boolean isEmailRegistered = isEmailAlreadyRegistered(email);

            if (isEmailRegistered && !isSameEmail) {
                Log.errorf("Email already registered: %s", email);
                throw new CustomerAlreadyExistsException("Email already registered: " + email);
            }

            if (isSameEmail) {
                Log.infof("Email is the same, no update needed: %s", email);
                return existingCustomer;
            }

            Log.infof("Email is available: %s", email);
            existingCustomer.setEmail(email);
        }

        String address = customer.getAddress();
        if (address != null && !address.isBlank()) existingCustomer.setAddress(address);

        String phone = customer.getPhone();
        if (phone != null && !phone.isBlank()) existingCustomer.setPhone(phone);

        return customerRepository.update(existingCustomer)
                .map(updatedCustomer -> {
                    Log.info("Customer updated successfully with ID: " + updatedCustomer.getId());
                    return updatedCustomer;
                })
                .orElseThrow(() -> {
                    Log.error("Failed to update customer with ID: " + existingCustomer.getId());
                    return new IllegalStateException("Error updating customer with ID: " + id);
                });
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id ID del cliente a eliminar
     * @return true si se eliminó correctamente, false si no se encontró el cliente
     * @throws CustomerNotFoundException si no se encuentra el cliente
     */
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