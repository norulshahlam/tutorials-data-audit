package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@Aspect
public class ContactChangeTracker {


    private final ContactRepository contactRepository;
    private final AuditMappedRepository auditMappedRepository;

    private static final ThreadLocal<Optional<ContactEntity>> previousStateHolder = ThreadLocal.withInitial(Optional::empty);

    public ContactChangeTracker(ContactRepository contactRepository, AuditMappedRepository auditMappedRepository) {
        this.contactRepository = contactRepository;
        this.auditMappedRepository = auditMappedRepository;
    }

    // Pointcut for createContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.createContact(..)) && args(contact)")
    public void createContactPointcut(ContactEntity contact) {
    }

    // Pointcut for editContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.editContact(..)) && args(contact)")
    public void editContactPointcut(ContactEntity contact) {
    }

    // Pointcut for deleteContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.deleteContact(..)) && args(id)")
    public void deleteContactPointcut(Long id) {
    }

    // Before advice for createContact
    @Before("createContactPointcut(contact)")
    public void trackCreateContactBefore(ContactEntity contact) {
        log.info("[BEFORE CREATE] Attempting to create ContactEntity: {}", contact);
    }

    // After advice for createContact
    @After("createContactPointcut(contact)")
    public void trackCreateContactAfter(ContactEntity contact) {
        log.info("[CREATE] ID: {}, Time: {}", contact.getId(), LocalDateTime.now());
        AuditMappedEntity audit = AuditMappedEntity.builder()
                .id(Math.toIntExact(contact.getId()))
                .commitId(BigDecimal.valueOf(System.currentTimeMillis()))
                .commitDate(LocalDateTime.now())
                .version(1L) // You can generate version dynamically or use a field from the entity
                .author("system") // Replace with the actual author from cookie or session
                .type("CREATE")
                .entity("ContactEntity")
                .build();

        auditMappedRepository.save(audit);
    }

    // Before advice for editContact
    @Before("editContactPointcut(contact)")
    public void trackEditContactBefore(ContactEntity contact) {
        log.info("[BEFORE UPDATE] Capturing previous state for ContactEntity with ID: {}", contact.getEmail());

        if (contact.getId() != null) {
            Optional<ContactEntity> byId = contactRepository.findById(contact.getId());

            if (byId.isPresent()) {
                // Deep copy before setting in ThreadLocal
                ContactEntity previousState = deepCopy(byId.get());
                previousStateHolder.set(Optional.of(previousState)); // Capture the state before edit
            }
        }
    }

    // After advice for editContact
    @After("editContactPointcut(contact)")
    public void trackEditContactAfter(ContactEntity contact) {
        Optional<ContactEntity> previousStateOpt = previousStateHolder.get();
        if (previousStateOpt.isPresent()) {
            ContactEntity previousState = previousStateOpt.get();
            if (!Objects.equals(previousState.getEmail(), contact.getEmail())) {
                log.info("[UPDATE] ID: {}, Time: {}, Field: email, Old Value: {}, New Value: {}",
                        contact.getId(), LocalDateTime.now(),
                        previousState.getEmail(), contact.getEmail());

                // Save Audit Record for Update
                AuditMappedEntity audit = AuditMappedEntity.builder()
                        .id(Math.toIntExact(contact.getId()))
                        .commitId(BigDecimal.valueOf(System.currentTimeMillis()))
                        .commitDate(LocalDateTime.now())
                        .version(1L) // You can generate version dynamically or use a field from the entity
                        .author("system") // Replace with the actual author from cookie or session
                        .type("UPDATE")
                        .fieldName("email")
                        .oldValue(previousState.getEmail())
                        .newValue(contact.getEmail())
                        .entity("ContactEntity")
                        .build();

                auditMappedRepository.save(audit);
            }
        }

        // Clear ThreadLocal after use
        previousStateHolder.remove();
    }

    // Before advice for deleteContact
    @Before("deleteContactPointcut(id)")
    public void trackDeleteContactBefore(Long id) {
        log.info("[BEFORE DELETE] Deleting ContactEntity with ID: {}", id);
    }

    // After advice for deleteContact
    @After("deleteContactPointcut(id)")
    public void trackDeleteContactAfter(Long id) {
        log.info("[DELETE] ID: {}, Time: {}", id, LocalDateTime.now());
    }

    // Deep copy method for ContactEntity to prevent modifications
    private ContactEntity deepCopy(ContactEntity original) {
        return ContactEntity.builder()
                .id(original.getId())
                .email(original.getEmail())
                .name(original.getName())
                .mobileNo(original.getMobileNo())
                .build();
    }
}
