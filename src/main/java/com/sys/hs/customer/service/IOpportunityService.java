package com.sys.hs.customer.service;

import java.util.List;

import com.sys.hs.customer.entity.Deal;
import com.sys.hs.customer.entity.Opportunity;

public interface IOpportunityService {

	public List<Deal> getAllDealList();

	public Deal getDealById(long id);
	
	public Opportunity save(Opportunity opportunity);
	
	public Opportunity update(Opportunity opportunity);
}
