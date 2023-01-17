package com.sys.hs.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.hs.customer.entity.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

	public Opportunity getByOptyId(long optyId);

}
