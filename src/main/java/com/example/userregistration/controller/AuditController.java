package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.json.JsonConverter;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/audit")
@Slf4j
public class AuditController {

    private final Javers javers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuditController(Javers javers) {
        this.javers = javers;
    }

    @GetMapping("/booking")
    public ResponseEntity<String> getBookingEntityChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(BookingEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint() + "</pre>");
    }

    @GetMapping("/contact")
    public ResponseEntity<String> getContactEntityChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(ContactEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint() + "</pre>");
    }

    @GetMapping("/contact/snapshots")
    public String getContactEntitySnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(ContactEntity.class);

        List<CdoSnapshot> changes = new ArrayList(javers.findSnapshots(jqlQuery.build()));

        changes.sort((o1, o2) -> -1 * o1.getCommitMetadata().getCommitDate().compareTo(o2.getCommitMetadata().getCommitDate()));

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

    @GetMapping("/contact/{id}")
    public String getContactEntityChanges(@PathVariable String id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, ContactEntity.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());

        return "<pre>" + changes.prettyPrint() + "</pre>";
    }
}
