package com.example.userregistration.controller;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.utils.Helper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
    private final AuditMappedRepository auditMappedRepository;

    public AuditController(AuditMappedRepository auditMappedRepository) {
        this.auditMappedRepository = auditMappedRepository;
    }

    @GetMapping("/getAuditReport")
    @Operation(summary = "Get contact audit report",
            description = "This endpoint will Get all contact audit")
    public ResponseEntity<List<AuditMappedEntity>> getAuditReport() {
        List<AuditMappedEntity> all = auditMappedRepository.findAll();
        if (ObjectUtils.isNotEmpty(all)) {
            helper.exportAsText(all);
        }
        return ResponseEntity.ok(new ArrayList<>());
    }
}
