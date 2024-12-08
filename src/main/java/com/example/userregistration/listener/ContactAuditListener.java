package com.example.userregistration.listener;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Slf4j
public class ContactAuditListener {

    @PreUpdate
    public void onPreUpdate(Object entity) {
        log.info("onPreUpdate: [{}]", entity);
    }

    @PrePersist
    public void onCreate(Object entity) {
        log.info("onCreate: [{}]", entity);
    }

    @PreRemove
    public void onDelete(Object entity) {
        log.info("onDelete: [{}]", entity);
    }

}
