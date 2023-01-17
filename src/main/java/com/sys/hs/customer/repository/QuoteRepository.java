package com.sys.hs.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.hs.customer.entity.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

}
