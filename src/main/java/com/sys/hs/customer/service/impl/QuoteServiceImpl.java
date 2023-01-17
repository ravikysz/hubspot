package com.sys.hs.customer.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sys.hs.customer.dto.QuoteRequestDTO;
import com.sys.hs.customer.entity.HSResponse;
import com.sys.hs.customer.entity.Quote;
import com.sys.hs.customer.repository.QuoteRepository;
import com.sys.hs.customer.service.IQuoteService;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QuoteServiceImpl implements IQuoteService {

	@Autowired
	private QuoteRepository quoteRepository;

	public Quote save(Quote quote) {
		return quoteRepository.save(quote);
	}

	@Override
	public Quote update(Quote quote) {
//		return quoteRepository.save(updateOpportunity);
		return null;
	}

	@Override
	public Quote saveQuoteDealById(QuoteRequestDTO quote) {

		// HubSpot Call to Create Quote
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth("pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492");

		Map<String, Map> propertiesMap = new HashMap<>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hs_title", quote.getName());
		map.put("hs_expiration_date", quote.getCloseDate());
		map.put("hs_currency", "USD");

		propertiesMap.put("properties", map);

		HSResponse hsQuoteResponse = new RestTemplate().postForEntity(UriComponentsBuilder.fromUri(UriComponentsBuilder
				.fromUriString("https://api.hubapi.com/crm/v3/objects/quotes").buildAndExpand().toUri()).build()
				.toUri(), new HttpEntity(propertiesMap, headers), HSResponse.class).getBody();

		// Update Quote associate with deal deal_to_quote
		new RestTemplate()
				.exchange(
						UriComponentsBuilder.fromUri(UriComponentsBuilder
								.fromUriString("https://api.hubapi.com/crm/v3/objects/deal/" + quote.getDealId()
										+ "/associations/quote/" + hsQuoteResponse.getId() + "/deal_to_quote")
								.buildAndExpand().toUri()).build().toUri(),
						HttpMethod.PUT, new HttpEntity(headers), HSResponse.class)
				.getBody();

		// Update Quote associate with template quote_to_quote_template
		new RestTemplate()
				.exchange(
						UriComponentsBuilder.fromUri(UriComponentsBuilder
								.fromUriString("https://api.hubapi.com/crm/v3/objects/quote/" + hsQuoteResponse.getId()
										+ "/associations/quote_template/165154431339/quote_to_quote_template")
								.buildAndExpand().toUri()).build().toUri(),
						HttpMethod.PUT, new HttpEntity(headers), HSResponse.class);

		// Create Product for Quote
		map.clear();
		map.put("price", 199);
		map.put("quantity", 1);
		map.put("name", "Product 1");

		propertiesMap.clear();
		propertiesMap.put("properties", map);

		HSResponse hsProductResponse = new RestTemplate()
				.postForEntity(
						UriComponentsBuilder.fromUri(
								UriComponentsBuilder.fromUriString("https://api.hubapi.com/crm/v3/objects/line_item")
										.buildAndExpand().toUri())
								.build().toUri(),
						new HttpEntity(propertiesMap, headers), HSResponse.class)
				.getBody();

		// Update Quote associate with Product line_item_to_quote
		new RestTemplate().exchange(
				UriComponentsBuilder.fromUri(UriComponentsBuilder
						.fromUriString("https://api.hubapi.com/crm/v3/objects/line_item/" + hsProductResponse.getId()
								+ "/associations/quote/" + hsQuoteResponse.getId() + "/line_item_to_quote")
						.buildAndExpand().toUri()).build().toUri(),
				HttpMethod.PUT, new HttpEntity(headers), HSResponse.class);

		// Update Quote path with its status
		map.clear();
		map.put("hs_status", "APPROVAL_NOT_NEEDED");

		propertiesMap.clear();
		propertiesMap.put("properties", map);

		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(2000);
		requestFactory.setReadTimeout(2000);
		restTemplate.setRequestFactory(requestFactory);

		restTemplate.exchange(
				UriComponentsBuilder.fromUri(UriComponentsBuilder
						.fromUriString("https://api.hubapi.com/crm/v3/objects/quote/" + hsQuoteResponse.getId())
						.buildAndExpand().toUri()).build().toUri(),
				HttpMethod.PATCH, new HttpEntity(propertiesMap, headers), HSResponse.class);

		Quote result = new Quote();
		result.setId(Long.parseLong(hsQuoteResponse.getId()));
		return result;
	}

}
