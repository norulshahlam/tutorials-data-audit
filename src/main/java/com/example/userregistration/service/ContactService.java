package com.example.userregistration.service;

import com.example.userregistration.entity.ContactEntity;

/**
 * @author norulshahlam.mohsen
 */
public interface ContactService {
    ContactEntity createContact(ContactEntity request);
    ContactEntity fetchContact(Long id);
    String deleteContact(Long id);
    ContactEntity editContact(ContactEntity contact);

}
