package com.sys.hs.customer.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.hs.customer.entity.Deal;
import com.sys.hs.customer.entity.Opportunity;
import com.sys.hs.customer.service.IOpportunityService;

@RestController
@RequestMapping("/deal")
public class OpportunityController {

	@Autowired
	private IOpportunityService opportunityService;

	@GetMapping
	Deal getDealById(@RequestParam("id") String id) {
		return opportunityService.getDealById(Long.parseLong(id));
	}

	@PostMapping
	Opportunity saveDeal(@RequestBody Opportunity opportunity) {
		return opportunityService.save(opportunity);
	}

	@PostMapping("/update")
	Opportunity updateDeal(@RequestBody Opportunity opportunity) {
		return opportunityService.update(opportunity);
	}
}
