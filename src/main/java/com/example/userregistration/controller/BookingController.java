package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.service.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Booking module")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized Access",
                content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "The server has not found anything matching the URI given",
                content = @Content),
        @ApiResponse(responseCode = "405", description = "Method not Allowed",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content),
        @ApiResponse(responseCode = "503", description = "Service Unavailable",
                content = @Content)})
public class BookingController {

    private final BookingService service;

    @ApiResponse(responseCode = "200", description = "Booking created",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Create new booking",
            description = "This endpoint will Create new booking based on the given input data")
    @PostMapping("createBooking")
    public ResponseEntity<BookingEntity> createBooking(@Valid @RequestBody @NotBlank BookingEntity request) throws JsonProcessingException {

        log.info("in BookingController::createBooking");
        BookingEntity booking = service.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @ApiResponse(responseCode = "200", description = "Booking fetched",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Fetch booking",
            description = "This endpoint will Fetch booking based on id")
    @GetMapping("fetchBooking/{id}")
    public ResponseEntity<BookingEntity> fetchBookingById(@PathVariable Long id) throws JsonProcessingException {

        log.info("in BookingController::fetchBooking");
        BookingEntity booking = service.fetchBookingById(id);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }


    @ApiResponse(responseCode = "200", description = "Booking fetched",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Fetch booking",
            description = "This endpoint will Fetch booking based on id")
    @GetMapping("fetchBookingByBkgRqstNo/{bkgRqstNo}")
    public ResponseEntity<BookingEntity> fetchBookingByBkgRqstNo(@PathVariable String bkgRqstNo) {

        log.info("in BookingController::fetchBooking");
        BookingEntity booking = service.fetchBookingByBkgRqstNo(bkgRqstNo);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }

    @ApiResponse(responseCode = "200", description = "Booking edited",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Edit existing user",
            description = "This endpoint will edit existing booking")
    @PostMapping("editBooking")
    public ResponseEntity<BookingEntity> editBooking(@Valid @RequestBody @NotBlank BookingEntity request) {
        log.info("in BookingController::editBooking");
        BookingEntity booking = service.editBooking(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(booking);
    }

    @ApiResponse(responseCode = "200", description = "Booking deleted",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Delete existing booking",
            description = "This endpoint will delete existing booking")
    @DeleteMapping("deleteBooking/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        log.info("in BookingController::deleteBooking");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.deleteBooking(id));
    }


    @ApiResponse(responseCode = "200", description = "contact created",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Create new contact",
            description = "This endpoint will Create new contact based on the given input data")
    @PostMapping("createContact")
    public ResponseEntity<ContactEntity> createContact(@Valid @RequestBody @NotBlank ContactEntity request) {

        log.info("in BookingController::createContact");
        ContactEntity booking = service.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @ApiResponse(responseCode = "200", description = "Contact fetched",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Fetch Contact",
            description = "This endpoint will Fetch Contact based on id")
    @GetMapping("fetchContact/{id}")
    public ResponseEntity<ContactEntity> fetchContact(@PathVariable Long id) {

        log.info("in BookingController::fetchContact");
        ContactEntity booking = service.fetchContact(id);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }

    @ApiResponse(responseCode = "200", description = "Contact Edited",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Edit existing contact",
            description = "This endpoint will Edit existing contact")
    @PostMapping("editContact")
    public ResponseEntity<ContactEntity> editContact(@Valid @RequestBody @NotBlank ContactEntity request) {
        log.info("in BookingController::editContact");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.editContact(request));
    }

    @ApiResponse(responseCode = "200", description = "Contact deleted",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Delete existing Contact",
            description = "This endpoint will delete existing contact")
    @DeleteMapping("deleteContact/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        log.info("in BookingController::deleteContact");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.deleteContact(id));
    }
}
