package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@Aspect
public class ContactChangeTracker {

    private final ContactRepository contactRepository;
    private final AuditMappedRepository auditMappedRepository;
    private final ExecutorService auditExecutor = Executors.newFixedThreadPool(10);
    private static final ThreadLocal<Optional<ContactEntity>> previousStateHolder = ThreadLocal.withInitial(Optional::empty);
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();


    public ContactChangeTracker(ContactRepository contactRepository, AuditMappedRepository auditMappedRepository) {
        this.contactRepository = contactRepository;
        this.auditMappedRepository = auditMappedRepository;
    }

    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.createContact(..)) && args(contact)")
    public void createContactPointcut(ContactEntity contact) {
    }

    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.editContact(..)) && args(contact)")
    public void editContactPointcut(ContactEntity contact) {
    }

    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.deleteContact(..)) && args(id)")
    public void deleteContactPointcut(Long id) {
    }

    @SneakyThrows
    @Around("createContactPointcut(contact)")
    public void trackCreateContactBefore(ProceedingJoinPoint joinPoint, ContactEntity contact) {
        log.info("[BEFORE CREATE] ContactEntity: {}", contact);

        ContactEntity newContact = (ContactEntity) joinPoint.proceed();

        log.info("[AFTER CREATE] ContactEntity: {}", newContact);
        logChangeAsync(newContact, null, "CREATE", null, null);
    }

    @Around("editContactPointcut(contact)")
    public void trackEditContactBefore(ProceedingJoinPoint joinPoint, ContactEntity contact) throws Throwable {
        log.info("[BEFORE UPDATE] Capturing previous state for ContactEntity with ID: {}", contact
                .getEmail());

        /* Get cookie */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        requestThreadLocal.set(attributes.getRequest());

        ContactEntity previousState = new ContactEntity();
        Optional<ContactEntity> byId = contactRepository.findById(contact.getId());
        if (byId.isPresent()) {
            // Deep copy
            previousState = deepCopy(byId.get());
        }

        ContactEntity updatedContact = (ContactEntity) joinPoint.proceed();

        /* Use Apache DiffBuilder to dynamically compare differences */
        DiffResult<ContactEntity> diffResult = new ReflectionDiffBuilder<>(updatedContact, previousState, ToStringStyle.DEFAULT_STYLE).build();

        List<AuditMappedEntity> update = diffResult.getDiffs().stream().map(i -> AuditMappedEntity.builder()
                .commitDate(LocalDateTime.now())
                .version(1L)
                .id(Math.toIntExact(updatedContact.getId()))
                .author(getUsernameFromCookie())
                .type("UPDATE")
                .fieldName(i.getFieldName())
                .newValue(i.getLeft().toString())
                .oldValue(i.getRight().toString())
                .entity(updatedContact.getClass().getSimpleName())
                .build()).toList();
        auditMappedRepository.saveAll(update);

        requestThreadLocal.remove();
    }

    @Async
    public void logChangeAsync(ContactEntity contact, String fieldName, String type, String oldValue, String newValue) {
        auditExecutor.submit(() -> logChange(contact, fieldName, type, oldValue, newValue));
    }

    private void logChange(ContactEntity contact, String fieldName, String type, String oldValue, String newValue) {

        log.info("[{}] ID: {}, Time: {}, Field: {}, Old Value: {}, New Value: {}",
                type, contact.getId(), LocalDateTime.now(), fieldName, oldValue, newValue);

        AuditMappedEntity auditBuilder = AuditMappedEntity.builder()
                .id(contact.getId() != null ? Math.toIntExact(contact.getId()) : null)
                .commitId(BigDecimal.valueOf(System.currentTimeMillis()))
                .commitDate(LocalDateTime.now())
                .version(1L)
                .author(getUsernameFromCookie())
                .type(type)
                .fieldName(fieldName)
                .oldValue(oldValue)
                .newValue(newValue)
                .entity(contact.getClass().getSimpleName())
                .build();
        auditMappedRepository.save(auditBuilder);
    }


    @Before("deleteContactPointcut(id)")
    public void trackDeleteContactBefore(Long id) {
        log.info("[BEFORE DELETE] Deleting ContactEntity with ID: {}", id);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        requestThreadLocal.set(attributes.getRequest());

    }

    @After("deleteContactPointcut(id)")
    public void trackDeleteContactAfter(Long id) {
        ContactEntity contact = new ContactEntity();
        contact.setId(id);
        logChangeAsync(contact, null, "DELETE", null, null);
        requestThreadLocal.remove();
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

    private String getUsernameFromCookie() {
        HttpServletRequest request = requestThreadLocal.get();
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return "SYSTEM_USERNAME";
    }
}
