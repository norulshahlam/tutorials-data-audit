package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContractEntity;
import com.example.userregistration.service.JaversService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.core.json.JsonConverter;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/audit")
@Slf4j
public class AuditController {

    private final Javers javers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JaversService javersService;

    public AuditController(Javers javers, JaversService javersService) {
        this.javers = javers;
        this.javersService = javersService;
    }

    @GetMapping("/booking")
    public ResponseEntity<String> getBookingEntityChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(BookingEntity.class);
        List<Change> changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body(javers.getJsonConverter().toJson(changes));
    }

    @GetMapping("/bookingPretty")
    public ResponseEntity<String> getBookingEntityChangesPretty() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(BookingEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint() + "</pre>");
    }

    @GetMapping("/bookingPretty/{id}")
    public ResponseEntity<String> getBookingEntityChangesPrettyWithChildById(
            @PathVariable Long id) {
        JqlQuery jqlQuery = QueryBuilder.byInstanceId(id, BookingEntity.class)
                .withChildValueObjects().build();
        Changes changes = javers.findChanges(jqlQuery);

        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint() + "</pre>");
    }


    @GetMapping("/contractsPretty/{id}")
    public ResponseEntity<String> getContractEntityChangesPretty(
            @PathVariable Long id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id,ContractEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint() + "</pre>");
    }

    @GetMapping("/contracts")
    public ResponseEntity<String> getContractEntityChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(ContractEntity.class);
        List<Change> changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body(javers.getJsonConverter().toJson(changes));
    }


    @GetMapping("/contracts/snapshots")
    public String getContractEntitySnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(ContractEntity.class);

        List<CdoSnapshot> changes = new ArrayList(javers.findSnapshots(jqlQuery.build()));

        changes.sort((o1, o2) -> -1 * o1.getCommitMetadata()
                .getCommitDate()
                .compareTo(o2.getCommitMetadata()
                        .getCommitDate()));

        JsonConverter jsonConverter = javers.getJsonConverter();

        return jsonConverter.toJson(changes);
    }

    @GetMapping("/booking/snapshots")
    public String getBookingEntitySnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(BookingEntity.class);

        List<CdoSnapshot> changes = new ArrayList(javers.findSnapshots(jqlQuery.build()));

        changes.sort((o1, o2) -> -1 * o1.getCommitMetadata().getCommitDate().compareTo(o2.getCommitMetadata().getCommitDate()));

        JsonConverter jsonConverter = javers.getJsonConverter();

        return jsonConverter.toJson(changes);
    }

    @GetMapping("/contracts/{id}")
    public String getContactEntityChanges(@PathVariable String id) {
        QueryBuilder jqlQuery = QueryBuilder
                .byInstanceId(id, ContractEntity.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());
        return "<pre>" + changes.prettyPrint() + "</pre>";
    }

    @GetMapping("/getShadowsWithScopeDeepPlusQuery")
    public List getShadowsWithScopeDeepPlusQuery(
            @RequestParam(defaultValue = "1") Long id,
            @RequestParam(defaultValue = "BookingEntity") String entityClass,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam Boolean isNextRecordRequired) throws ClassNotFoundException {

        Class<?> className = Class.forName("com.example.userregistration.entity." + entityClass);
        log.info("className: {}", className.getName());
        return javersService.getShadowsWithScopeDeepPlusQuery(id, className, page, pageSize, isNextRecordRequired);
    }

    @GetMapping("/getShadowsWithShadowScopeQuery")
    public List getShadowsWithShadowScopeQuery(
            @RequestParam(defaultValue = "1") Long id,
            @RequestParam(defaultValue = "BookingEntity") String entityClass,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam Boolean isNextRecordRequired) throws ClassNotFoundException {

        Class<?> className = Class.forName("com.example.userregistration.entity." + entityClass);
        log.info("className: {}", className.getName());
        return javersService.getShadowsWithShadowScopeQuery(id, className, page, pageSize, isNextRecordRequired);
    }
}
