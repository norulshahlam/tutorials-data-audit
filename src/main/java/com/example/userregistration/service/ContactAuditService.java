package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        AuditMappedEntity mappedEntity = mapCreatedAndDeleted(entity, "CREATED");
        auditMappedRepository.save(mappedEntity);
    }

    public void logUpdateContact(ContactEntity updatedEntity, ContactEntity originalEntity) {
        List<AuditMappedEntity> changes = new ArrayList<>();

        if (!Objects.equals(originalEntity.getName(), updatedEntity.getName())) {
            changes.add(buildFieldChangeAudit(
                    updatedEntity, "name", originalEntity.getName(), updatedEntity.getName()
            ));
        }

        if (!Objects.equals(originalEntity.getEmail(), updatedEntity.getEmail())) {
            changes.add(buildFieldChangeAudit(
                    updatedEntity, "email", originalEntity.getEmail(), updatedEntity.getEmail()
            ));
        }

        if (!changes.isEmpty()) {
            auditMappedRepository.saveAll(changes);
        }
    }



    public void logDeleteContact(ContactEntity entity) {
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

    /**
     * Helper method to build an AuditMappedEntity for field changes.
     */
    private AuditMappedEntity buildFieldChangeAudit(ContactEntity entity, String fieldName, String oldValue, String newValue) {
        return AuditMappedEntity.builder()
                .id(Math.toIntExact(entity.getId()))
                .type("UPDATED")
                .commitDate(LocalDateTime.now())
                .entity(entity.getClass().getSimpleName())
                .fieldName(fieldName)
                .oldValue(oldValue)
                .newValue(newValue)
                .author("SomeUser") // Replace with actual user tracking logic if needed
                .build();
    }
}
