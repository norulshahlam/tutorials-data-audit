package com.example.userregistration.service;

import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContactAuditService {

    private final ContactRepository contactRepository;

    public ContactAuditService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


}
