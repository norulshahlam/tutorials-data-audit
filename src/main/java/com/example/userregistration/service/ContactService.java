package com.example.userregistration.service;

import com.example.userregistration.entity.ContactEntity;

import java.util.List;

/**
 * @author norulshahlam.mohsen
 */
public interface ContactService {
    ContactEntity createContact(ContactEntity request);
    ContactEntity fetchContact(Long id);
    String deleteContact(Long id);
    ContactEntity editContact(ContactEntity contact);

    List<ContactEntity> saveMultipleContacts(List<ContactEntity> contactEntities);

}
