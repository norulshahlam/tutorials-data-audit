package com.example.userregistration.controller;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.entity.ContractEntity;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.service.JaversService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Changes;
import org.javers.core.ChangesByCommit;
import org.javers.core.Javers;
import org.javers.core.JaversCoreProperties;
import org.javers.core.commit.CommitId;
import org.javers.core.diff.Change;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bad Request",
                    content = @Content(examples = {
                            @ExampleObject(name = "Changes found",
                                    summary = "Changes found",
                                    description = "Changes found",
                                    value = """
                                            [Commit 43.00 done by anonymousUser at 27 Nov 2023, 09:34:12 :
                                            * changes on com.mypil.usermgmt.domain.entities.CompanyEntity/18 :
                                              - 'updatedDate' changed: 'Mon Nov 27 09:32:39 SGT 2023' -> 'Mon Nov 27 09:34:12 SGT 2023'
                                            * changes on com.mypil.usermgmt.domain.entities.UserRegistrationEntity/64 :
                                              - 'updatedDate' changed: 'Mon Nov 27 09:32:39 SGT 2023' -> 'Mon Nov 27 09:34:12 SGT 2023'
                                            * changes on com.mypil.usermgmt.domain.entities.UserRoleEntity/31 :
                                              - 'roleCode' changed: 'Bill To Party' -> 'Bill To 11'
                                              - 'updatedDate' changed: 'Mon Nov 27 09:32:39 SGT 2023' -> 'Mon Nov 27 09:34:12 SGT 2023'
                                            , Commit 42.00 done by anonymousUser at 27 Nov 2023, 09:32:39 :
                                            * changes on com.mypil.usermgmt.domain.entities.CompanyEntity/18 :
                                              - 'updatedDate' changed: 'Mon Nov 27 08:59:53 SGT 2023' -> 'Mon Nov 27 09:32:39 SGT 2023'
                                            * changes on com.mypil.usermgmt.domain.entities.UserRegistrationEntity/64 :
                                              - 'firstName' changed: 'ss' -> '59ujkyv'
                                              - 'updatedDate' changed: 'Mon Nov 27 08:59:53 SGT 2023' -> 'Mon Nov 27 09:32:39 SGT 2023'
                                            * changes on com.mypil.usermgmt.domain.entities.UserRoleEntity/31 :
                                              - 'updatedDate' changed: 'Mon Nov 27 08:59:53 SGT 2023' -> 'Mon Nov 27 09:32:39 SGT 2023']
                                                                                    """)
                    }, mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Booking not found",
                    content = @Content)})
    @Operation(summary = "Retrieve changes made to booking along its child entities. Default number of commits is at 10",
            description = "Retrieve changes made to booking along its child entities. Default number of commits is at 10",
            tags = {"Audit"})
    @GetMapping("/getBookingCommitsById/{id}")
    public ResponseEntity<?> getBookingCommitsById(
            @PathVariable Long id, @RequestParam(required = false, value = "limit", defaultValue = "10") Integer limit) {

        log.info("Getting changes for booking with id: {}", id);

        /* Get Booking entity */
        Optional<BookingEntity> registrationEntity = bookingRepository.findById(id);
        if (registrationEntity.isPresent()) {
            LinkedList<Change> changes = new LinkedList<>();

            getEntityChanges(QueryBuilder.byInstanceId(registrationEntity.get().getId(), BookingEntity.class), changes);

            /* Get contract entity changes */
            if (ObjectUtils.isNotEmpty(registrationEntity.get().getContracts())) {
                registrationEntity.get().getContracts().forEach(entity -> {
                    getEntityChanges(QueryBuilder.byInstanceId(entity.getId(), ContractEntity.class), changes);
                });
            }

            /* Get contact entity changes */
            if (ObjectUtils.isNotEmpty(registrationEntity.get().getContacts())) {
                registrationEntity.get().getContacts().forEach(entity -> {
                    getEntityChanges(QueryBuilder.byInstanceId(entity.getId(), ContactEntity.class), changes);
                });
            }
            if (CollectionUtils.isEmpty(changes)) {
                return ResponseEntity.ok().body("No changes found for booking with id: " + id);
            }
            /* Create Changes from the added changes while limit number of commits. default limit is 10  */
            List<ChangesByCommit> changesByCommits = new Changes(
                    changes, new PrettyValuePrinter(
                    new JaversCoreProperties.PrettyPrintDateFormats()))
                    .groupByCommit().stream().limit(limit).toList();

            /* Alternative way to create Changes. This is being customizable to fine-tune your structure, while the above doesn't. */
            Changes collect = changes.stream()
                    .filter(change -> change.getCommitMetadata().isPresent())
                    .sorted(Comparator.comparing(
                            i -> i.getCommitMetadata().get().getId(),
                            Comparator.comparing(CommitId::getMajorId).reversed()))
                    .collect(Collectors.collectingAndThen(Collectors.toList(),
                            sortedList -> new Changes(sortedList, new PrettyValuePrinter(
                                    new JaversCoreProperties.PrettyPrintDateFormats()))));

            return ResponseEntity.ok().body(collect.prettyPrint());
        }
        return ResponseEntity.ok().body("No changes found for booking with id: " + id);
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

    private void getEntityChanges(QueryBuilder contractEntity, LinkedList<Change> changes) {
        JqlQuery contractJqlQuery = contractEntity
                .withChildValueObjects().build();
        Changes contractChanges = javers.findChanges(contractJqlQuery);
        changes.addAll(contractChanges);
    }
}
