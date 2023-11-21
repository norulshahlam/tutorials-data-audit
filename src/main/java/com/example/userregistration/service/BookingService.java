package com.example.userregistration.service;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;

/**
 * @author norulshahlam.mohsen
 */
public interface BookingService {

    BookingEntity createBooking(BookingEntity booking);

    BookingEntity editBooking(BookingEntity booking);
    String deleteBooking(Long id);
    String deleteContact(Long id);

    ContactEntity editContact(ContactEntity contact);

    BookingEntity fetchBooking(Long id);

    ContactEntity createContact(ContactEntity request);

    ContactEntity fetchContact(Long id);
}
