package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.entity.ContractEntity;
import com.example.userregistration.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles(profiles = "mysql")
@WebMvcTest(controllers = BookingController.class)
@Slf4j
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    Faker faker = new Faker();

    @RepeatedTest(10)
    void createBooking() throws Exception {

        ContractEntity contractEntity = ContractEntity.builder()
                .frtTermCode(faker.phoneNumber().cellPhone())
                .status(faker.letterify("??????"))
                .build();
        ContactEntity contactEntity = ContactEntity.builder()
                .name(faker.name().fullName())
                .mobileNo(faker.phoneNumber().cellPhone())
                .build();
        BookingEntity bookingEntity = BookingEntity.builder()
                .bkgRqstNo(faker.phoneNumber().cellPhone())
                .contacts(List.of(contactEntity))
                .contracts(List.of(contractEntity))
                .build();

        log.info("bookingEntity: \n" + bookingEntity);

        ResultActions createBooking = mockMvc.perform(
                        post("/api/v1/createBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingEntity)))
                .andExpect(status().isCreated());
        log.info("createBooking: " + createBooking);

    }

    @Test
    @Disabled
    void fetchBooking() {
    }

    @Test
    @Disabled
    void editBooking() {
    }

    @Test
    @Disabled
    void deleteBooking() {
    }

    @Test
    @Disabled
    void editContact() {
    }
}