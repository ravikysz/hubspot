package com.sys.hs.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.hs.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
