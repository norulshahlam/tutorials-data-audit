package com.example.userregistration;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author norulshahlam.mohsen
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class TutorialsDataAuditApplication implements CommandLineRunner {

    private final ContactRepository contactRepository;

    public TutorialsDataAuditApplication(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TutorialsDataAuditApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Creating new contact..");
        ContactEntity contactEntity = contactRepository.save(ContactEntity.builder()
                .name("Tutorials")
                .email("www@abc.com")
                .mobileNo("92212152")
                .build());

        log.info("Editing existing contact..");
        contactEntity.setName("Tutorials2");
        contactEntity.setEmail("www.abc.abc");
        ContactEntity contactEntity1 = contactRepository.save(contactEntity);

        log.info("Deleting existing contact..");
        contactRepository.delete(contactEntity1);
    }
}
