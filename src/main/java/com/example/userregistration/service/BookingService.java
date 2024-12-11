package com.example.userregistration.service;

import com.example.userregistration.entity.BookingEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author norulshahlam.mohsen
 */
public interface BookingService {

    BookingEntity createBooking(BookingEntity booking) throws JsonProcessingException;

    BookingEntity editBooking(BookingEntity booking);
    String deleteBooking(Long id);


    BookingEntity fetchBookingById(Long id) throws JsonProcessingException;


    BookingEntity fetchBookingByBkgRqstNo(String bkgRqstNo);
}
