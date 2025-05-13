package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import com.vvelc.customers.infrastructure.persistence.panache.CustomerPanacheRepository;
import com.vvelc.customers.infrastructure.persistence.entity.CustomerEntity;
import com.vvelc.customers.infrastructure.persistence.mapper.CustomerMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerPanacheRepository customerPanacheRepository;

    public CustomerRepositoryImpl(CustomerPanacheRepository customerPanacheRepository) {
        this.customerPanacheRepository = customerPanacheRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = CustomerMapper.toEntity(customer);
        customerPanacheRepository.persist(entity);
        return CustomerMapper.toDomain(entity);
    }

    @Override
    public List<Customer> findAll(int page, int size) {
        return customerPanacheRepository.findAll().page(page, size).list().stream()
                .map(CustomerMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerPanacheRepository.findByIdOptional(id)
                .map(CustomerMapper::toDomain);
    }

    @Override
    public List<Customer> findByCountry(String country, int page, int size) {
        return customerPanacheRepository.find("country", country).page(page, size).list().stream()
                .map(CustomerMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Customer> update(Customer customer) {
        return customerPanacheRepository.findByIdOptional(customer.getId())
                .map(entity -> {
                            entity.setEmail(customer.getEmail());
                            entity.setAddress(customer.getAddress());
                            entity.setPhone(customer.getPhone());
                            entity.setCountry(customer.getCountry());
                            entity.setDemonym(customer.getDemonym());
                            return CustomerMapper.toDomain(entity);
                        }
                );
    }

    @Override
    public boolean deleteById(Long id) {
        return customerPanacheRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return customerPanacheRepository.count();
    }

    @Override
    public Long countByCountry(String country) {
        return customerPanacheRepository.count("country", country);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerPanacheRepository.count("email", email) > 0;
    }
}
