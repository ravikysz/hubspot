package com.sys.hs.customer.controller;

import com.sys.hs.customer.dto.QuoteRequestDTO;
import com.sys.hs.customer.entity.Quote;
import com.sys.hs.customer.repository.QuoteRepository;
import com.sys.hs.customer.service.IQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    @Autowired
    private IQuoteService quoteService;

    @Autowired
    private QuoteRepository quoteRepository;

    @PostMapping
    Quote saveQuote(@RequestBody Quote quote) {
        return quoteService.save(quote);
    }

    @PostMapping("/update")
    Quote updateQuote(@RequestBody Quote quote) {
        return quoteService.update(quote);
    }

    @PostMapping("/saveQuoteById")
    Quote saveQuoteByDealId(@RequestBody QuoteRequestDTO quote) {
        return quoteService.saveQuoteDealById(quote);
    }

}
