package com.example.userregistration.impl;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.repository.ContactRepository;
import com.example.userregistration.service.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * @author norulshahlam.mohsen
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ContactRepository contactRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BookingEntity createBooking(BookingEntity booking) throws JsonProcessingException {
        log.info("in BookingServiceImpl::createBooking");
        return bookingRepository.save(booking);
    }

    @Override
    public BookingEntity fetchBookingById(Long id) throws JsonProcessingException {
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);

        if (bookingEntity.isPresent()) {
            log.info("bookingEntity: " + objectMapper.writeValueAsString(bookingEntity.get()));
            return bookingEntity.get();
        }
        throw new NoResultException("Booking to be fetched not found");
    }

    @Override
    public BookingEntity editBooking(BookingEntity booking) {

        Optional<BookingEntity> result = bookingRepository.findById(booking.getId());

        if (result.isPresent()) {
            log.info("in BookingServiceImpl::editBooking");
            BookingEntity bookingEntity = result.get();

            updatedToEntity(booking, bookingEntity);

            BookingEntity saved = bookingRepository.save(bookingEntity);
            log.info("saved: " + saved);
            return saved;
        }
        throw new NoResultException("Booking to be edited not found");
    }

    private static void updatedToEntity(BookingEntity booking, BookingEntity bookingEntity) {
        bookingEntity.setBkgRqstNo(booking.getBkgRqstNo());
        bookingEntity.setBkgRqstStatusSeq(booking.getBkgRqstStatusSeq());
        bookingEntity.setBkgNo(booking.getBkgNo());

        bookingEntity.getContracts().clear();
        bookingEntity.getContracts().addAll(booking.getContracts());

        bookingEntity.getContacts().clear();
        bookingEntity.getContacts().addAll(booking.getContacts());

    }

    @Override
    public String deleteBooking(Long id) {
        Optional<BookingEntity> result = bookingRepository.findById(id);

        if (result.isPresent()) {
            BookingEntity bookingEntity = result.get();
            bookingRepository.delete(bookingEntity);
            return "Booking deleted successfully";
        }
        throw new NoResultException("Booking to be deleted not found");
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
    public BookingEntity fetchBookingByBkgRqstNo(String bkgRqstNo) {
        BookingEntity byBkgRqstNo = bookingRepository.findByBkgRqstNo(bkgRqstNo);
        if (ObjectUtils.isNotEmpty(byBkgRqstNo)) {
            return byBkgRqstNo;
        }
        throw new NoResultException("Booking to be fetched not found");
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


    /**
     * Change only name and mobileNo
     *
     * @param contact
     * @return
     */
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
