package com.sys.hs.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.hs.customer.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
