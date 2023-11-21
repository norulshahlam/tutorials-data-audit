package com.example.userregistration.service;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author norulshahlam.mohsen
 */
public interface BookingService {

    BookingEntity createBooking(BookingEntity booking);

    BookingEntity editBooking(BookingEntity booking) throws JsonProcessingException;
    String deleteBooking(Long id);
    String deleteContact(Long id);

    ContactEntity editContact(ContactEntity contact);

    BookingEntity fetchBooking(Long id);

    ContactEntity createContact(ContactEntity request);

    ContactEntity fetchContact(Long id);
}
