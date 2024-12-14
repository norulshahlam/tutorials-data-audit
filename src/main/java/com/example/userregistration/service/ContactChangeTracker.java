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
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Aspect
public class ContactChangeTracker {
    private final ContactRepository contactRepository;
    private final AuditMappedRepository auditMappedRepository;
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

    @Pointcut("execution(* com.example.userregistration.impl.ContactServiceImpl.saveMultipleContacts(..)) && args(contactEntities)")
    public void saveMultipleContactsPointcut(List<ContactEntity> contactEntities) {
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Around("saveMultipleContactsPointcut(contactEntities)")
    public void trackSaveMultipleContacts(ProceedingJoinPoint joinPoint, List<ContactEntity> contactEntities) {
        log.info("[BEFORE BULK CREATE] Attempting to create multiple contacts");

        /* Get cookie */
        getServletAttributes();

        List<ContactEntity> createdContactLists = (List<ContactEntity>) joinPoint.proceed();

        List<AuditMappedEntity> mappedContactLists = createdContactLists.stream()
                .map(i -> AuditMappedEntity.builder()
                        .commitDate(LocalDateTime.now())
                        .version(1L)
                        .id(Math.toIntExact(i.getId()))
                        .author(getUsernameFromCookie())
                        .type("CREATE")
                        .entity(i.getClass().getSimpleName())
                        .build()).toList();
        auditMappedRepository.saveAll(mappedContactLists);
        log.info("[AFTER BULK CREATE] Attempting to create multiple contacts");

        requestThreadLocal.remove();
    }

    @SneakyThrows
    @Around("createContactPointcut(contact)")
    public void trackCreateContactAround(ProceedingJoinPoint joinPoint, ContactEntity contact) {
        log.info("[BEFORE CREATE]");

        ContactEntity newContact = (ContactEntity) joinPoint.proceed();

        logChange(newContact, null, "CREATE", null, null);
        log.info("[AFTER CREATE ID: {}]",newContact.getId());
    }

    @SneakyThrows
    @Around("editContactPointcut(contact)")
    public void trackEditContactAround(ProceedingJoinPoint joinPoint, ContactEntity contact) {
        log.info("[BEFORE UPDATE] ID: {}", contact.getId());

        /* Get cookie */
        getServletAttributes();

        ContactEntity previousState = new ContactEntity();
        Optional<ContactEntity> byId = contactRepository.findById(contact.getId());
        if (byId.isPresent()) {
            // Deep copy
            previousState = deepCopy(byId.get());
        }

        ContactEntity updatedContact = (ContactEntity) joinPoint.proceed();

        /* Use Apache DiffBuilder to dynamically compare differences */
        DiffResult<ContactEntity> diffResult = new ReflectionDiffBuilder<>(updatedContact, previousState, ToStringStyle.DEFAULT_STYLE).build();

        List<AuditMappedEntity> update = diffResult.getDiffs()
                .stream()
                .map(i -> AuditMappedEntity.builder()
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
        log.info("[AFTER UPDATE] ID: {}", contact.getId());
        requestThreadLocal.remove();
    }

    @SneakyThrows
    @Around("deleteContactPointcut(id)")
    public void trackDeleteContactAround(ProceedingJoinPoint joinPoint, Long id) {
        log.info("[BEFORE DELETE] ID: {}", id);

        joinPoint.proceed();

        ContactEntity contact = new ContactEntity();
        contact.setId(id);
        logChange(contact, null, "DELETE", null, null);
        log.info("[AFTER DELETE] ID: {}", id);
    }


    private void logChange(ContactEntity contact, String fieldName, String type, String oldValue, String newValue) {

        getServletAttributes();

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
        requestThreadLocal.remove();
    }

    private static void getServletAttributes() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        requestThreadLocal.set(attributes.getRequest());
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
