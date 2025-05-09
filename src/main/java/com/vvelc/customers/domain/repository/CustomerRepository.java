package com.vvelc.customers.domain.repository;

import com.vvelc.customers.domain.model.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository  {
    void save(Customer booking);
    List<Customer> findAll(int page, int size);
    List<Customer> findByCountry(String country, int page, int size);
    Optional<Customer> findById(Long id);
    Optional<Customer> update(Customer customer);
    Long count();
    Long countByCountry(String country);
    boolean deleteById(Long id);
}