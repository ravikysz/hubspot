package com.sys.hs.customer.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "customer_contact")
public class Contact implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	private String firstname;
	private String email;
	private String lastname;
	private long customerId;
	private String department;
	private String phone;
	private boolean primaryFlag;
}
