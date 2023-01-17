package com.sys.hs.customer.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SfOpportunities {
    private String opportunityId;
    private String optyName;
    private String type;
    private Instant createdDate;
    private Instant modifiedDate;

}