package com.example.userregistration.service;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author norulshahlam.mohsen
 */
public interface BookingService {

    BookingEntity createBooking(BookingEntity booking) throws JsonProcessingException;

    BookingEntity editBooking(BookingEntity booking);
    String deleteBooking(Long id);
    String deleteContact(Long id);

    ContactEntity editContact(ContactEntity contact);

    BookingEntity fetchBookingById(Long id) throws JsonProcessingException;

    ContactEntity createContact(ContactEntity request);

    ContactEntity fetchContact(Long id);

    BookingEntity fetchBookingByBkgRqstNo(String bkgRqstNo);
}
