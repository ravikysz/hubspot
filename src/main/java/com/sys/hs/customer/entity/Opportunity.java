package com.sys.hs.customer.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "opportunity")
public class Opportunity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String optyName;
	private String optyDesc;
	private String optyType;
	private String optyStageName;
	private double amount;
	private String businessName;
	private long cdRefId;
	private Instant createdDate;
	private String currency;
	private String custContactRole;
	private Instant lastModifiedDate;
	@Column(unique = true)
	private long optyId;
	private Instant optyStartDate;
	private long optyOwnerId;
	private String region;
}
