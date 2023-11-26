package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.entity.ContractEntity;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.service.JaversService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversCoreProperties;
import org.javers.core.commit.CommitId;
import org.javers.core.diff.Change;
import org.javers.core.json.JsonConverter;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/audit")
@Slf4j
public class AuditController {

    private final Javers javers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final BookingRepository bookingRepository;

    private final JaversService javersService;

    public AuditController(Javers javers, BookingRepository bookingRepository, JaversService javersService) {
        this.javers = javers;
        this.bookingRepository = bookingRepository;
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
        log.info("changes: {}\n", changes);
        return ResponseEntity.ok().body(changes.prettyPrint());
    }

    @GetMapping("/contractsPretty/{id}")
    public ResponseEntity<String> getContractEntityChangesPretty(
            @PathVariable Long id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, ContractEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        log.info("changes: {}\n", changes);
        return ResponseEntity.ok().body("<pre>" + changes.prettyPrint());
    }

    @GetMapping("/allPretty")
    public ResponseEntity<String> getAllEntityChangesPretty() {
        QueryBuilder jqlQuery = QueryBuilder.anyDomainObject();
        Changes changes = javers.findChanges(jqlQuery.build());
        log.info("changes: {}\n", changes);
        return ResponseEntity.ok().body("<pre>" + changes.groupByCommit());
    }

    @GetMapping("/combinedEntities/{id}")
    public ResponseEntity<String> getAllEntityCombinedChangesPretty(
            @PathVariable Long id) throws JsonProcessingException {

        /* Get Booking entity */
        JqlQuery bookingJqlQuery = QueryBuilder.byInstanceId(id, BookingEntity.class)
                .withChildValueObjects().build();
        Changes bookingChanges = javers.findChanges(bookingJqlQuery);

        log.info("bookingChanges: {}\n", bookingChanges.prettyPrint());

        LinkedList<Change> changes = new LinkedList<>(bookingChanges);
        bookingRepository.findById(id).ifPresent(bookingEntity -> {

            /* Get contract entity */
            if (!CollectionUtils.isEmpty(bookingEntity.getContracts())) {
                bookingEntity.getContracts().forEach(contractEntity -> {
                    JqlQuery contractJqlQuery = QueryBuilder.byInstanceId(contractEntity.getId(), ContractEntity.class).withChildValueObjects().build();
                    Changes contractChanges = javers.findChanges(contractJqlQuery);
                    log.info("contractChanges: {}\n", contractChanges.prettyPrint());
                    changes.addAll(contractChanges);
                });
            }
            /* Get contract entity */
            if (!CollectionUtils.isEmpty(bookingEntity.getContacts())) {
                bookingEntity.getContacts().forEach(contactEntity -> {
                    JqlQuery contactJqlQuery = QueryBuilder.byInstanceId(contactEntity.getId(), ContactEntity.class).withChildValueObjects().build();
                    Changes contactChanges = javers.findChanges(contactJqlQuery);
                    log.info("contactChanges: {}\n", contactChanges.prettyPrint());
                    changes.addAll(contactChanges);
                });
            }
        });
        log.info("changes: {}\n", changes);

        Changes combinedChanges = changes.stream()
                .filter(change -> change.getCommitMetadata().isPresent())
                .sorted(Comparator.comparing(
                        i -> i.getCommitMetadata().get().getId(),
                        Comparator.comparing(CommitId::getMajorId).reversed()))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        sortedList -> new Changes(sortedList, new PrettyValuePrinter(new JaversCoreProperties.PrettyPrintDateFormats()))));

        log.info("combinedChanges: {}\n", combinedChanges.prettyPrint());

        return ResponseEntity.ok().body("<pre>" + combinedChanges.prettyPrint());
    }

    @GetMapping("/booking/snapshots")
    public String getBookingEntitySnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(BookingEntity.class);
        List<CdoSnapshot> changes = new ArrayList(javers.findSnapshots(jqlQuery.build()));

        changes.sort((o1, o2) -> -1 * o1.getCommitMetadata().getCommitDate().compareTo(o2.getCommitMetadata().getCommitDate()));

        JsonConverter jsonConverter = javers.getJsonConverter();
        return jsonConverter.toJson(changes);
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
