package com.example.userregistration.listener;

import javax.persistence.PreUpdate;

public class AuditListener {

    @PreUpdate
    public void onPreUpdate(Object entity) {
        System.out.println(11);
        System.out.println(entity);

    }
}
