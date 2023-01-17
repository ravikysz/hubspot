package com.sys.hs.customer.entity;

import java.util.Map;

import lombok.Data;

@Data
public class HSResponse {

	private String id;
	private Map<String, Object> properties;
	private Map<String, Object> propertiesWithHistory;
	private String createdAt;
	private String updatedAt;
	private Boolean archived;
	private String archivedAt;
	private Map<String, Object> associations;

}
