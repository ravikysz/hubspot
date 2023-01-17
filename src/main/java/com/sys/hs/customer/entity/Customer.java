package com.sys.hs.customer.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	private String type;
	private String segment;
}
