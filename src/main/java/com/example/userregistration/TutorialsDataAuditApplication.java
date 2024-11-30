package com.example.userregistration;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Arrays;
import java.util.List;

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

        log.info("Creating 2 new contact..");
        ContactEntity contactEntity = ContactEntity.builder()
                .name("Tutorials")
                .email("www@abc.com")
                .mobileNo("92212152")
                .build();

        ContactEntity contactEntity1 = ContactEntity.builder()
                .name("John Doe")
                .email("www@abc2.com")
                .mobileNo("9221215222")
                .build();

        List<ContactEntity> savedContacts = contactRepository.saveAll(Arrays.asList(contactEntity, contactEntity1));

        log.info("Editing 2 existing contact..");

        savedContacts.get(0).setName("Tutorials2");
        savedContacts.get(0).setEmail("www.abc.abc2");

        savedContacts.get(1).setName("Tutorials3");
        savedContacts.get(1).setEmail("www.abc.abc3");

        List<ContactEntity> editedContacts = contactRepository.saveAll(savedContacts);

        log.info("Deleting existing contact..");
        contactRepository.deleteAll(editedContacts);
    }
}
