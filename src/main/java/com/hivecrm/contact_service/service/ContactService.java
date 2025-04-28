package com.hivecrm.contact_service.service;

import com.hivecrm.contact_service.dto.ContactRequest;
import com.hivecrm.contact_service.mapper.ContactMapper;
import com.hivecrm.contact_service.model.Contact;
import com.hivecrm.contact_service.repository.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    public Contact create(String userId, ContactRequest request) {
        Contact contact = contactMapper.map(request);
            contact.setUserId(userId);
        return contactRepository.save(contact);
    }

    public List<Contact> getAllByUser(String userId) {
        return contactRepository.findByUserId(userId);
    }

    public Page<Contact> getContactsByPage(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName"));
        return contactRepository.findByUserId(userId, pageable);
    }


    public Contact update(String id, ContactRequest request) {
        Contact contact = contactRepository.findById(id).orElseThrow();

        Contact updatedContact = contactMapper.map(request);
        updatedContact.setId(contact.getId());
        updatedContact.setUserId(contact.getUserId());

        return contactRepository.save(updatedContact);
    }

    public void delete(String id) {
        contactRepository.deleteById(id);
    }
}

