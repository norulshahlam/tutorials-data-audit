package com.example.userregistration.listener;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.service.ContactAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
@Slf4j
@Component
public class ContactAuditListener {

    private final ObjectProvider<ContactAuditService> contactAuditServiceProvider;

    public ContactAuditListener(ObjectProvider<ContactAuditService> contactAuditServiceProvider) {
        this.contactAuditServiceProvider = contactAuditServiceProvider;
    }

    @PostPersist
    public void onPostPersist(ContactEntity entity) {
        log.info("onPostPersist: [{}]", entity);
        contactAuditServiceProvider.getIfAvailable().logCreateContact(entity);
    }

    @PostUpdate
    public void onPostUpdate(ContactEntity entity) {
        log.info("onPostUpdate: [{}]", entity);
        contactAuditServiceProvider.getIfAvailable().logUpdateContact(entity);
    }

    @PostRemove
    public void onPostRemove(ContactEntity entity) {
        log.info("onPostRemove: [{}]", entity);
        contactAuditServiceProvider.getIfAvailable().logDeleteContact(entity);
    }
}
