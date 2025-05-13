package com.vvelc.customers.domain.repository;

import com.vvelc.customers.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository  {
    Customer save(Customer customer);
    List<Customer> findAll(int page, int size);
    List<Customer> findByCountry(String country, int page, int size);
    Optional<Customer> findById(Long id);
    Optional<Customer> update(Customer customer);
    Long count();
    Long countByCountry(String country);
    boolean existsByEmail(String email);
    boolean deleteById(Long id);
}