package com.sys.hs.customer.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class QuoteRequestDTO {
	
	private String name;
	private String currency;
	private Instant createdDate;
	private String closeDate;
	private long dealId;

}
