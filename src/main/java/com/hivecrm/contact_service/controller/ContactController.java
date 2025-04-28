package com.hivecrm.contact_service.controller;

import com.hivecrm.contact_service.dto.ContactRequest;
import com.hivecrm.contact_service.model.Contact;
import com.hivecrm.contact_service.service.ContactExportService;
import com.hivecrm.contact_service.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;
    private final ContactExportService contactExportService;

    public ContactController(ContactService contactService, ContactExportService contactExportService) {
        this.contactService = contactService;
        this.contactExportService = contactExportService;
    }

    @PostMapping
    public ResponseEntity<Contact> create(@RequestHeader("userId") String userId, @RequestBody ContactRequest contactRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.create(userId, contactRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAll(@RequestHeader("userId") String userId) {
        return ResponseEntity.ok()
                .body(contactService.getAllByUser(userId));
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getPaginatedContacts(@RequestHeader("userId") String userId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok()
                .body(contactService.getContactsByPage(page, size, userId));
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

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportContactsToExcel(@RequestHeader("userId") String userId) {
        try {
            byte[] exportedData = contactExportService.exportToExcel(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("contacts.xlsx").build());

            return new ResponseEntity<>(exportedData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    /**
     *
     * Adding for testing purpose
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadContacts(@RequestHeader("userId") String userId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }
        contactExportService.uploadContacts(userId, file);
        return ResponseEntity.ok("Contacts uploaded successfully!");
    }
}

