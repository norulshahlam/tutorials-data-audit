package com.example.userregistration.impl;

import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.ContactRepository;
import com.example.userregistration.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Optional;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactEntity createContact(ContactEntity request) {
        log.info("in BookingServiceImpl::createContact");
        return contactRepository.save(request);
    }

    @Override
    public ContactEntity fetchContact(Long id) {
        Optional<ContactEntity> contactEntity = contactRepository.findById(id);
        if (contactEntity.isPresent()) {
            return contactEntity.get();
        }
        throw new NoResultException("Contact to be fetched not found");
    }

    @Override
    public String deleteContact(Long id) {
        Optional<ContactEntity> result = contactRepository.findById(id);

        if (result.isPresent()) {
            ContactEntity contactEntity = result.get();
            contactRepository.delete(contactEntity);
            return "Booking deleted successfully";
        }
        throw new NoResultException("Contact to be deleted not found");
    }

    @Override
    public ContactEntity editContact(ContactEntity contact) {
        Optional<ContactEntity> result = contactRepository.findById(contact.getId());

        if (result.isPresent()) {
            log.info("in BookingServiceImpl::editContact");
            ContactEntity contactEntity = result.get();
            log.info("contactEntity: " + contactEntity);

            BeanUtils.copyProperties(contact, contactEntity, "id");

            return contactRepository.save(contactEntity);
        }
        throw new NoResultException("Contact to be edited not found");
    }

}
