package com.example.userregistration.listener;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.service.ContactAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
@Service
public class ContactAuditListener {

    private final ApplicationContext applicationContext;

    public ContactAuditListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostPersist
    public void onPostPersist(ContactEntity entity) {
        log.info("onPostPersist: [{}]", entity);
        applicationContext.getBean(ContactAuditService.class).logCreateContact(entity);
    }

    @PostUpdate
    public void onPostUpdate(ContactEntity entity) {
        log.info("onPostUpdate: [{}]", entity);
        applicationContext.getBean(ContactAuditService.class).logUpdateContact(entity);
    }


    @PostRemove
    public void onPostRemove(ContactEntity entity) {
        log.info("onPostRemove: [{}]", entity);
        applicationContext.getBean(ContactAuditService.class).logDeleteContact(entity);
    }

}
