package com.sys.hs.customer.service.impl;

import com.sys.hs.customer.entity.*;
import com.sys.hs.customer.repository.OpportunityRepository;
import com.sys.hs.customer.service.IOpportunityService;
import com.sys.hs.customer.service.IQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class OpportunityServiceImpl implements IOpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private IQuoteService quoteService;

    @Override
    public List<Deal> getAllDealList() {
        return new ArrayList<>();
    }

    @Override
    public Deal getDealById(long id) {

        // HubSpot Call to Get Deal By Id
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("pat-na1-230fe4e2-eab8-4734-a572-ff51c3e77492");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("archived", true);

        HttpEntity request = new HttpEntity(requestMap, headers);

        URI uri = UriComponentsBuilder
                .fromUri(UriComponentsBuilder.fromUriString("https://api.hubapi.com/crm/v3/objects/deals/" + id)
                        .buildAndExpand(requestMap).toUri())
                .queryParam("associations", "quote").queryParam("associations", "contact").build().toUri();

        HSResponse hsDealResponse = new RestTemplate().exchange(uri, HttpMethod.GET, request, HSResponse.class)
                .getBody();

        Deal deal = new Deal();
        deal.setId(id);
        deal.setName(hsDealResponse.getProperties().get("dealname").toString());

        Map<String, Object> associationMap = hsDealResponse.getAssociations();
        List<Map<String, Object>> quoteMapLst = (List<Map<String, Object>>) (((Map<String, List>) associationMap
                .get("quotes")).get("results")).stream()
                .filter(map -> (((Map<String, Object>) map).get("type").toString().equals("deal_to_quote")))
                .collect(Collectors.toList());

        List<Map<String, Object>> contactMapLst = (List<Map<String, Object>>) (((Map<String, List>) associationMap
                .get("contacts")).get("results")).stream()
                .filter(map -> (((Map<String, Object>) map).get("type").toString().equals("deal_to_contact")))
                .collect(Collectors.toList());

        List<Quote> quotes = new ArrayList();
        for (Map result : quoteMapLst) {
            Quote quote = new Quote();
            uri = UriComponentsBuilder.fromUri(UriComponentsBuilder
                    .fromUriString("https://api.hubapi.com/crm/v3/objects/quotes/" + result.get("id"))
                    .buildAndExpand(requestMap).toUri()).build().toUri();
            HSResponse hsQuoteResponse = new RestTemplate().exchange(uri, HttpMethod.GET, request, HSResponse.class)
                    .getBody();
            quote.setId(Long.parseLong(result.get("id").toString()));
            quote.setName(hsQuoteResponse.getProperties().get("hs_title").toString());
            quotes.add(quote);
        }

        List<Contact> contacts = new ArrayList();
        for (Map result : contactMapLst) {
            Contact contact = new Contact();
            uri = UriComponentsBuilder.fromUri(UriComponentsBuilder
                    .fromUriString("https://api.hubapi.com/crm/v3/objects/contacts/" + result.get("id"))
                    .buildAndExpand(requestMap).toUri()).build().toUri();
            HSResponse hsContactResponse = new RestTemplate().exchange(uri, HttpMethod.GET, request, HSResponse.class)
                    .getBody();
            contact.setId(Long.parseLong(result.get("id").toString()));
            contact.setFirstname(hsContactResponse.getProperties().get("firstname").toString());
            contact.setEmail(hsContactResponse.getProperties().get("email").toString());
            contacts.add(contact);
        }

        deal.setQuotes(quotes);
        deal.setContacts(contacts);
        return deal;
    }

    public Opportunity save(Opportunity opportunity) {
        Opportunity opty = opportunityRepository.save(opportunity);

        getDealById(opty.getOptyId());

//		 Create default quotation
//        QuoteRequestDTO quote = new QuoteRequestDTO();
//        quote.setCloseDate(String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
//                .plus(15, ChronoUnit.DAYS).toEpochMilli()));
//        quote.setName(opty.getOptyName() + " - Quote");
//        quote.setCurrency(opty.getCurrency());
//        quote.setDealId(opty.getOptyId());
//
//        quoteService.saveQuoteDealById(quote);
        return opty;
    }

    @Override
    public Opportunity update(Opportunity opportunity) {
        Opportunity updateOpportunity = opportunityRepository.getByOptyId(opportunity.getOptyId());
        updateOpportunity.setOptyName(opportunity.getOptyName());
        updateOpportunity.setOptyStageName(opportunity.getOptyStageName());
        updateOpportunity.setAmount(opportunity.getAmount());
        updateOpportunity.setOptyDesc(opportunity.getOptyDesc());
        updateOpportunity.setOptyType(opportunity.getOptyType());
        updateOpportunity.setLastModifiedDate(opportunity.getLastModifiedDate());
        return opportunityRepository.save(updateOpportunity);
    }
}