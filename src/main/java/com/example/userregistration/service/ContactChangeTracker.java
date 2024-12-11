package com.example.userregistration.service;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@Aspect
public class ContactChangeTracker {

    @Autowired
    private ContactRepository contactRepository;

    private static final ThreadLocal<Optional<ContactEntity>> previousStateHolder = ThreadLocal.withInitial(Optional::empty);

    // Pointcut for createContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.createContact(..)) && args(contact)")
    public void createContactPointcut(ContactEntity contact) {}

    // Pointcut for editContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.editContact(..)) && args(contact)")
    public void editContactPointcut(ContactEntity contact) {}

    // Pointcut for deleteContact
    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.deleteContact(..)) && args(id)")
    public void deleteContactPointcut(Long id) {}

    // Before advice for createContact
    @Before("createContactPointcut(contact)")
    public void trackCreateContactBefore(ContactEntity contact) {
        log.info("[BEFORE CREATE] Attempting to create ContactEntity: {}", contact);
    }

    // After advice for createContact
    @After("createContactPointcut(contact)")
    public void trackCreateContactAfter(ContactEntity contact) {
        log.info("[CREATE] ID: {}, Time: {}", contact.getId(), LocalDateTime.now());
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
        // Create a new ContactEntity and copy values
        ContactEntity copy = new ContactEntity();
        copy.setId(original.getId());
        copy.setEmail(original.getEmail());
        copy.setName(original.getName());
        copy.setMobileNo(original.getMobileNo());
        // Add other fields here if needed

        return copy;
    }
}
