package com.sys.hs.customer.entity;

import java.util.List;

import lombok.Data;

@Data
public class Deal {

	private long id;
	private String name;
	private List<Quote> quotes;
	private List<Contact> contacts;

}
