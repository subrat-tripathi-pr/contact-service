package com.hivecrm.contact_service.controller;

import com.hivecrm.contact_service.dto.ContactRequest;
import com.hivecrm.contact_service.model.Contact;
import com.hivecrm.contact_service.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> create(@RequestHeader("userId") String userId, @RequestBody ContactRequest contactRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.create(userId, contactRequest));
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAll(@RequestHeader("userId") String userId) {
        return ResponseEntity.ok()
                .body(contactService.getAllByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> update(@PathVariable String id, @RequestBody ContactRequest contactRequest) {
        return ResponseEntity.ok()
                .body(contactService.update(id, contactRequest));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        contactService.delete(id);
    }
}

