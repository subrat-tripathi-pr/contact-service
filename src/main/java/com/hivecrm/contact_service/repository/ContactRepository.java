package com.hivecrm.contact_service.repository;

import com.hivecrm.contact_service.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactRepository extends MongoRepository<Contact, String> {
    List<Contact> findByUserId(String userId);
    Page<Contact> findByUserId(String userId, Pageable page);
}

