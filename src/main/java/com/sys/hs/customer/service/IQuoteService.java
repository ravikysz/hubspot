package com.sys.hs.customer.service;

import com.sys.hs.customer.dto.QuoteRequestDTO;
import com.sys.hs.customer.entity.Quote;

public interface IQuoteService {

	public Quote save(Quote quote);
	
	public Quote update(Quote quote);
	
	public Quote saveQuoteDealById(QuoteRequestDTO quote);
}
