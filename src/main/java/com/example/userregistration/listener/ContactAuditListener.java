package com.example.userregistration.listener;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.service.ContactAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;

@Slf4j
@Component
public class ContactAuditListener {

    private final ObjectProvider<ContactAuditService> contactAuditServiceProvider;
    private static final ThreadLocal<ContactEntity> originalState = new ThreadLocal<>();

    public ContactAuditListener(ObjectProvider<ContactAuditService> contactAuditServiceProvider) {
        this.contactAuditServiceProvider = contactAuditServiceProvider;
    }

    @PostPersist
    public void onPostPersist(ContactEntity entity) {
        log.info("Audit::onPostPersist: [{}]", entity);
        contactAuditServiceProvider.getIfAvailable().logCreateContact(entity);
    }

    @PreUpdate
    public synchronized void onPreUpdate(ContactEntity entity) {
        log.info("Audit::onPreUpdate: Capturing original state for entity: [{}]", entity);
        // Create a deep copy to store the original state
        ContactEntity copy = ContactEntity.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .mobileNo(entity.getMobileNo())
                .build();
        originalState.set(copy);
    }

    @PostUpdate
    public void onPostUpdate(ContactEntity entity) {
        log.info("Audit::onPostUpdate: [{}]", entity);
        ContactEntity originalEntity = originalState.get();
        if (originalEntity != null) {
            contactAuditServiceProvider.getIfAvailable().logUpdateContact(entity, originalEntity);
            originalState.remove(); // Clean up thread-local storage
        }
    }

    @PostRemove
    public void onPostRemove(ContactEntity entity) {
        log.info("Audit::onPostRemove: [{}]", entity);
        contactAuditServiceProvider.getIfAvailable().logDeleteContact(entity);
    }
}
