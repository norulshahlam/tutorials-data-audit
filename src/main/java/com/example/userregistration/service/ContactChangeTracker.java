package com.example.userregistration.service;


import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class ContactChangeTracker {

    @Autowired
    private ContactRepository contactRepository;

    private static final ThreadLocal<ContactEntity> previousStateHolder = new ThreadLocal<>();

    // Pointcut for save
    @Pointcut("execution(* com.example.userregistration.repository.ContactRepository.save(..)) && args(contact)")
    public void savePointcut(ContactEntity contact) {
        System.out.println(1);
    }

    @Before("savePointcut(contact)")
    public void capturePreviousStateForSave(ContactEntity contact) {
        System.out.println(2);
        if (contact.getId() != null) {
            contactRepository.findById(contact.getId()).ifPresent(previousStateHolder::set);
            log.info("previousStateHolder; [{}]", previousStateHolder.get());
        }
    }

    @AfterReturning(pointcut = "savePointcut(contact)", returning = "result")
    @Transactional
    public void logSave(ContactEntity contact, ContactEntity result) {
        System.out.println(3);
        ContactEntity previousState = previousStateHolder.get();
        if (previousState != null && !Objects.equals(previousState.getEmail(), result.getEmail())) {
            log.info("[UPDATE] ID: {}, Time: {}, Field: email, Old Value: {}, New Value: {}",
                    result.getId(), LocalDateTime.now(),
                    previousState.getEmail(), result.getEmail());
        } else if (previousState == null) {
            log.info("[CREATE] ID: {}, Time: {}", result.getId(), LocalDateTime.now());
        }
        previousStateHolder.remove();
    }




    // Pointcut for saveAll
    @Pointcut("execution(* com.example.userregistration.repository.ContactRepository.saveAll(..)) && args(contactEntities)")
    public void saveAllPointcut(List<ContactEntity> contactEntities) {
        System.out.println(4);
    }

    @AfterReturning(pointcut = "saveAllPointcut(contactEntities)", returning = "result")
    @Transactional
    public void logSaveAll(List<ContactEntity> contactEntities, List<ContactEntity> result) {
        System.out.println(5);
        for (ContactEntity savedEntity : result) {
            if (savedEntity.getId() == null) {
                log.info("[CREATE] ID: {}, Time: {}", savedEntity.getId(), LocalDateTime.now());
            }
        }
    }



    // Pointcut for delete
    @Pointcut("execution(* com.example.userregistration.repository.ContactRepository.delete(..)) && args(contact)")
    public void deletePointcut(ContactEntity contact) {
        System.out.println(6);
    }

    @AfterReturning(pointcut = "deletePointcut(contact)")
    public void logDelete(ContactEntity contact) {
        System.out.println(7);
        log.info("[DELETE] ID: {}, Time: {}", contact.getId(), LocalDateTime.now());
    }
}


