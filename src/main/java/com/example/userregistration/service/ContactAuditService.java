package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ContactAuditService {

    private final AuditMappedRepository auditMappedRepository;
    private final ContactRepository contactRepository;

    public ContactAuditService(AuditMappedRepository auditMappedRepository, ContactRepository contactRepository) {
        this.auditMappedRepository = auditMappedRepository;
        this.contactRepository = contactRepository;
    }

    public void logCreateContact(ContactEntity entity) {
        log.info("Creating contact: [{}]", entity);
        AuditMappedEntity mappedEntity = mapCreatedAndDeleted(entity, "CREATED");
        auditMappedRepository.save(mappedEntity);
    }


    public void logUpdateContact(ContactEntity entity) {
        log.info("Updating contact: [{}]", entity);
        Optional<ContactEntity> byId = contactRepository.findById(entity.getId());
        byId.ifPresent(contactEntity -> log.info("Contact found: [{}]", contactEntity));
    }

    public void logDeleteContact(ContactEntity entity) {
        log.info("Deleting contact: [{}]", entity);
        AuditMappedEntity mappedEntity = mapCreatedAndDeleted(entity, "DELETED");
        auditMappedRepository.save(mappedEntity);
    }

    private AuditMappedEntity mapCreatedAndDeleted(ContactEntity entity, String event) {
        return auditMappedRepository.save(AuditMappedEntity.builder()
                .id(Math.toIntExact(entity.getId()))
                .type(event)
                .commitDate(LocalDateTime.now())
                .entity(entity.getClass().getSimpleName())
                .build());
    }

}
