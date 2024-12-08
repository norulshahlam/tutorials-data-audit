package com.example.userregistration.listener;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Slf4j
@Service
public class ContactAuditListener {

    private final Helper helper;
    private final AuditMappedRepository auditRepository;

    public ContactAuditListener(Helper helper, AuditMappedRepository auditRepository) {
        this.helper = helper;
        this.auditRepository = auditRepository;
    }


    @PostUpdate
    public void onPostUpdate(ContactEntity entity) {
        log.info("onPostUpdate: [{}]", entity);
    }

    @PostPersist
    public void onPostPersist(ContactEntity entity) {
        log.info("onPostPersist: [{}]", entity);
    }

    @PostRemove
    public void onPostRemove(ContactEntity entity) {
        log.info("onPostRemove: [{}]", entity);
    }

}
