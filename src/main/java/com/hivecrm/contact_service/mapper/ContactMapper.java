package com.hivecrm.contact_service.mapper;

import com.hivecrm.contact_service.dto.ContactRequest;
import com.hivecrm.contact_service.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    Contact map(ContactRequest request);
}
