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
        bookingEntity.setSiNo(booking.getSiNo());
        bookingEntity.setBkgOfficeCode(booking.getBkgOfficeCode());
        bookingEntity.setHandleOfficeCode(booking.getHandleOfficeCode());
        bookingEntity.setVslCode(booking.getVslCode());
        bookingEntity.setSkdVoyNo(booking.getSkdVoyNo());
        bookingEntity.setSkdDirCode(booking.getSkdDirCode());
        bookingEntity.setVslName(booking.getVslName());
        bookingEntity.setPorCode(booking.getPorCode());
        bookingEntity.setPorName(booking.getPorName());
        bookingEntity.setPolCode(booking.getPolCode());
        bookingEntity.setPolName(booking.getPolName());
        bookingEntity.setPodCode(booking.getPodCode());
        bookingEntity.setPodName(booking.getPodName());
        bookingEntity.setDelCode(booking.getDelCode());
        bookingEntity.setDelName(booking.getDelName());
        bookingEntity.setPkgQty(booking.getPkgQty());
        bookingEntity.setPkgTypeCode(booking.getPkgTypeCode());
        bookingEntity.setMeasQty(booking.getMeasQty());
        bookingEntity.setMeasUnitCode(booking.getMeasUnitCode());
        bookingEntity.setNetWgt(booking.getNetWgt());
        bookingEntity.setNetWgtUnitCode(booking.getNetWgtUnitCode());
        bookingEntity.setNetMeasQty(booking.getNetMeasQty());
        bookingEntity.setNetMeasUnitCode(booking.getNetMeasUnitCode());
        bookingEntity.setDcFlg(booking.getDcFlg());
        bookingEntity.setRcFlg(booking.getRcFlg());
        bookingEntity.setAwkCgoFlg(booking.getAwkCgoFlg());
        bookingEntity.setRqstDate(booking.getRqstDate());
        bookingEntity.setRqstDeltFlg(booking.getRqstDeltFlg());
        bookingEntity.setBkgUpldStatusCode(booking.getBkgUpldStatusCode());
        bookingEntity.setRqstDepDate(booking.getRqstDepDate());
        bookingEntity.setRqstArrDate(booking.getRqstArrDate());
        bookingEntity.setBkgRmks1(booking.getBkgRmks1());
        bookingEntity.setBkgRmks2(booking.getBkgRmks2());
        bookingEntity.setAutoCfmEdiFlg(booking.getAutoCfmEdiFlg());
        bookingEntity.setAutoEmailFlg(booking.getAutoEmailFlg());
        bookingEntity.setGdsDesc(booking.getGdsDesc());
        bookingEntity.setMkDesc(booking.getMkDesc());
        bookingEntity.setRqstAcptUsrId(booking.getRqstAcptUsrId());
        bookingEntity.setRqstAcptUpdDate(booking.getRqstAcptUpdDate());
        bookingEntity.setObTrspModCode(booking.getObTrspModCode());
        bookingEntity.setIbTrspModCode(booking.getIbTrspModCode());
        bookingEntity.setTemplateName(booking.getTemplateName());
        bookingEntity.setDeleteFlag(booking.getDeleteFlag());
        bookingEntity.setBlNo(booking.getBlNo());
        bookingEntity.setSiRqstStatusCode(booking.getSiRqstStatusCode());
        bookingEntity.setBlRdyTpCode(booking.getBlRdyTpCode());
        bookingEntity.setBlCpyKnt(booking.getBlCpyKnt());
        bookingEntity.setOblRdemKnt(booking.getOblRdemKnt());
        bookingEntity.setBlRdyOfficeCode(booking.getBlRdyOfficeCode());
        bookingEntity.setBlRdyUsrId(booking.getBlRdyUsrId());
        bookingEntity.setBlRdyDate(booking.getBlRdyDate());
        bookingEntity.setDgRmks(booking.getDgRmks());
        bookingEntity.setActVoyNo(booking.getActVoyNo());
        bookingEntity.setStatus(booking.getStatus());
        bookingEntity.setTransitTime(booking.getTransitTime());
        bookingEntity.setVoyageCode(booking.getVoyageCode());
        bookingEntity.setService(booking.getService());
        bookingEntity.setGateInCutoffDate(booking.getGateInCutoffDate());
        bookingEntity.setVgmCutoffDate(booking.getVgmCutoffDate());
        bookingEntity.setDocumentationCutoffDate(booking.getDocumentationCutoffDate());
        bookingEntity.setDocTpCode(booking.getDocTpCode());
        bookingEntity.setBkgRqstsStatusCode(booking.getBkgRqstsStatusCode());

        bookingEntity.setBkgNo(booking.getBkgNo());
        bookingEntity.setBkgRqstStatusSeq(booking.getBkgRqstStatusSeq());

        bookingEntity.getContracts().clear();
        bookingEntity.getContracts().addAll(booking.getContracts());

        bookingEntity.getContacts().clear();
        bookingEntity.getContacts().addAll(booking.getContacts());

        bookingEntity.getCustomers().clear();
        bookingEntity.getCustomers().addAll(booking.getCustomers());

        bookingEntity.getCommodities().clear();
        bookingEntity.getCommodities().addAll(booking.getCommodities());

        bookingEntity.getCustomers().clear();
        bookingEntity.getCustomers().addAll(booking.getCustomers());

        bookingEntity.getEquipments().clear();
        bookingEntity.getEquipments().addAll(booking.getEquipments());

        bookingEntity.getDangerousGoods().clear();
        bookingEntity.getDangerousGoods().addAll(booking.getDangerousGoods());

        bookingEntity.getVesselVoyages().clear();
        bookingEntity.getVesselVoyages().addAll(booking.getVesselVoyages());

        bookingEntity.getRefeerCargos().clear();
        bookingEntity.getRefeerCargos().addAll(booking.getRefeerCargos());
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
