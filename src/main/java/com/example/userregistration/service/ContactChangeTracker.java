package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
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

    @Before("editContactPointcut(contact)")
    public void trackEditContactBefore(ContactEntity contact) {
        log.info("[BEFORE UPDATE] Capturing previous state for ContactEntity with ID: {}", contact
                .getEmail());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        requestThreadLocal.set(attributes.getRequest());

        if (contact.getId() != null) {
            Optional<ContactEntity> byId = contactRepository.findById(contact.getId());

            if (byId.isPresent()) {
                // Deep copy before setting in ThreadLocal
                ContactEntity previousState = deepCopy(byId.get());
                previousStateHolder.set(Optional.of(previousState)); // Capture the state before edit
            }
        }
    }

    @After("editContactPointcut(contact)")
    public void trackEditContactAfter(ContactEntity contact) {
        Optional<ContactEntity> previousStateOpt = previousStateHolder.get();
        if (previousStateOpt.isPresent()) {
            ContactEntity previousState = previousStateOpt.get();

            // Check and log changes for email
            if (!Objects.equals(previousState.getEmail(), contact.getEmail())) {
                logChangeAsync(contact, "email", "UPDATE", previousState.getEmail(), contact.getEmail());
            }

            // Check and log changes for mobileNo
            if (!Objects.equals(previousState.getMobileNo(), contact.getMobileNo())) {
                logChangeAsync(contact, "mobileNo", "UPDATE", previousState.getMobileNo(), contact.getMobileNo());
            }
        }
        requestThreadLocal.remove();

        // Clear ThreadLocal after use
        previousStateHolder.remove();
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
                .version(1L) // You can generate version dynamically or use a field from the entity
                .author(getUsernameFromCookie()) // Replace with the actual author from cookie or session
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
