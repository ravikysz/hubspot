package com.sys.hs.customer.controller;

import com.sys.hs.customer.entity.Deal;
import com.sys.hs.customer.entity.Opportunity;
import com.sys.hs.customer.service.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deal")
public class OpportunityController {

    @Autowired
    private IOpportunityService opportunityService;

    @GetMapping
    Deal getDealById(@RequestParam("id") String id) {
        return opportunityService.getDealById(Long.parseLong(id));
    }

    @GetMapping("/hello")
    String hello() {
        return "Hello World";
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
