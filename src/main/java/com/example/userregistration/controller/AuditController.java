package com.example.userregistration.controller;

import com.example.userregistration.entity.AuditLog;
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

    private final Helper helper;
    private final BookingRepository bookingRepository;

    public AuditController(Helper helper, BookingRepository bookingRepository) {
        this.helper = helper;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/contactsPretty")
    @Operation(summary = "Get all contact audit",
            description = "This endpoint will Get all contact audit")
    public ResponseEntity<List<AuditLog>> getContractEntityChangesPrettyAll() {

        return ResponseEntity.ok(new ArrayList<>());
    }


}
