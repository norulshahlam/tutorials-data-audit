package com.example.userregistration.controller;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.utils.Helper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/audit")
@Slf4j
public class AuditController {

    private Helper helper;
    private final BookingRepository bookingRepository;

    public AuditController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @GetMapping("/contactsPretty")
    @Operation(summary = "Get all contact audit",
            description = "This endpoint will Get all contact audit")
    public ResponseEntity<List<AuditMappedEntity>> getContractEntityChangesPrettyAll() {

        return ResponseEntity.ok(new ArrayList<>());
    }


}
