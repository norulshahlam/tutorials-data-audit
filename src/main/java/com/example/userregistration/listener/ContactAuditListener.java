package com.example.userregistration.listener;

import com.example.userregistration.entity.ContactEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
@Service
public class ContactAuditListener {


    @PostUpdate
    public void onPreUpdate(ContactEntity entity) {
        log.info("onPreUpdate: [{}]", entity);
    }

    @PostPersist
    public void onCreate(ContactEntity entity) {
        log.info("onCreate: [{}]", entity);
    }

    @PostRemove
    public void onDelete(ContactEntity entity) {
        log.info("onDelete: [{}]", entity);
    }

}
