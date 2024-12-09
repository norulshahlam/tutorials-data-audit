package com.example.userregistration.service;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContactAuditService {

    private final AuditMappedRepository auditLog;
    private final ContactRepository contactRepository;

    public ContactAuditService(AuditMappedRepository auditLog, ContactRepository contactRepository) {
        this.auditLog = auditLog;
        this.contactRepository = contactRepository;
    }

    public void logDeleteContact(ContactEntity entity) {
        log.info("Deleting contact: [{}]", entity);
    }
}
