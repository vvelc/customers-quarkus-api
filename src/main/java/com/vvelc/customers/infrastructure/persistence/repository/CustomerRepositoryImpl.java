package com.vvelc.customers.infrastructure.persistence.repository;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import com.vvelc.customers.infrastructure.persistence.panache.CustomerPanacheRepository;
import com.vvelc.customers.infrastructure.persistence.entity.CustomerEntity;
import com.vvelc.customers.infrastructure.persistence.mapper.CustomerMapper;
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
    public void save(Customer order) {
        CustomerEntity entity = CustomerMapper.toEntity(order);
        customerPanacheRepository.persist(entity);
    }

    @Override
    public List<Customer> findAll() {
        return customerPanacheRepository.findAll().list().stream()
                .map(CustomerMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerPanacheRepository.findByIdOptional(id)
                .map(CustomerMapper::toDomain);
    }

    @Override
    public List<Customer> findByCountry(String country) {
        return customerPanacheRepository.find("country", country).list().stream()
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
                            return entity;
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
}
