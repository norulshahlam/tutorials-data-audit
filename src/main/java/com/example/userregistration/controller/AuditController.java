package com.example.userregistration.controller;

import com.example.userregistration.entity.AuditMapped;
import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.service.JaversService;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/audit")
@Slf4j
public class AuditController {

    private final Javers javers;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BookingRepository bookingRepository;
    private final JaversService javersService;
    private final AuditMappedRepository auditMappedRepository;

    public AuditController(Javers javers, BookingRepository bookingRepository, JaversService javersService, AuditMappedRepository auditMappedRepository) {
        this.javers = javers;
        this.bookingRepository = bookingRepository;
        this.javersService = javersService;
        this.auditMappedRepository = auditMappedRepository;
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
        return ResponseEntity.ok().body(changes.prettyPrint() + "</pre>");
    }

    @GetMapping("/bookingPretty/{id}")
    @Operation(summary = "Get booking audit by ID",
            description = "This endpoint will Get booking audit by ID")
    public ResponseEntity<String> getBookingEntityChangesPrettyWithChildById(
            @PathVariable Long id) {
        JqlQuery jqlQuery = QueryBuilder.byInstanceId(id, BookingEntity.class)
                .withChildValueObjects().build();
        Changes changes = javers.findChanges(jqlQuery);
        log.info("changes: {}\n", changes);
        return ResponseEntity.ok().body(changes.prettyPrint());
    }

    @GetMapping("/contactsPretty/{id}")
    @Operation(summary = "Get contact audit by ID",
            description = "This endpoint will Get contact audit by ID")
    public ResponseEntity<String> getContractEntityChangesPrettyById(
            @PathVariable Long id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, ContactEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());

        List<ChangesByCommit> changesByCommits = changes.groupByCommit();
        log.info("changesByCommits: {}\n", changesByCommits);
        return ResponseEntity.ok().body(changes.prettyPrint());
    }

    @GetMapping("/contactsPretty")
    @Operation(summary = "Get all contact audit",
            description = "This endpoint will Get all contact audit")
    public ResponseEntity<List<AuditMapped>> getContractEntityChangesPrettyAll() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(ContactEntity.class);
        Changes changes = javers.findChanges(jqlQuery.build());
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());

        if (!changes.isEmpty()) {
            List<AuditMapped> mappedList = customizeAuditDetails(changes, snapshots);
            exportAsText(mappedList);
//            mappedList.forEach(i -> log.info("mappedList: \n{}", i));
            return ResponseEntity.ok().body(mappedList);
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/allPretty")
    public ResponseEntity<String> getAllEntityChangesPretty() {
        QueryBuilder jqlQuery = QueryBuilder.anyDomainObject();
        Changes changes = javers.findChanges(jqlQuery.build());
        String changed = changes.groupByCommit().toString();
        log.info("changes: {}\n", changed);
        return ResponseEntity.ok().body(changed);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changes found",
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
                    }, mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Booking not found",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "No changes found",
                    content = @Content)})
    @Operation(summary = "Retrieve changes made to booking along its child entities. Default number of change commits is at 10",
            description = "Retrieve changes made to booking along its child entities. Default number of change commits is at 10",
            tags = {"Audit"})
    @GetMapping("/getBookingCommitsById/{id}")
    public ResponseEntity<?> getBookingCommitsById(
            @PathVariable Long id,
            @RequestParam(required = false, value = "limit", defaultValue = "10")
            @Parameter(description = "limit number of change commits. Default is 10 or lesser if total commit is less than 10",
                    name = "limit") Integer limit) {

        log.info("Getting changes for booking with id: {}", id);

        /* Get Booking entity */
        Optional<BookingEntity> registrationEntity = bookingRepository.findById(id);
        if (registrationEntity.isPresent()) {
            LinkedList<Change> changes = new LinkedList<>();

            getEntityChanges(QueryBuilder.byInstanceId(registrationEntity.get().getId(), BookingEntity.class), changes);


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

            /* Alternative way to create Changes.
            This can be customized to fine-tune your custom structure, while the above doesn't. */
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


    @Hidden
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

    @Hidden
    @GetMapping("/getShadowsWithShadowScopeQuery")
    public List getShadowsWithShadowScopeQuery(
            @RequestParam(defaultValue = "1") Long id,
            @RequestParam(defaultValue = "BookingEntity") String entityClass,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam Boolean isNextRecordRequired) throws ClassNotFoundException {

        Class<?> className = Class.forName("com.example.userregistration.entity." + entityClass);
        return javersService.getShadowsWithShadowScopeQuery(id, className, page, pageSize, isNextRecordRequired);
    }

    private void getEntityChanges(QueryBuilder contractEntity, LinkedList<Change> changes) {
        JqlQuery contractJqlQuery = contractEntity
                .withChildValueObjects().build();
        Changes contractChanges = javers.findChanges(contractJqlQuery);
        changes.addAll(contractChanges);
    }

    private List<AuditMapped> customizeAuditDetails(Changes changes, List<CdoSnapshot> snapshots) {

        // Map commitId (majorId) to version
        Map<Long, Long> commitIdToVersion = snapshots.stream()
                .collect(Collectors.toMap(snapshot -> snapshot.getCommitMetadata().getId().getMajorId(), CdoSnapshot::getVersion));

        List<AuditMapped> mappedList = new ArrayList<>();

        changes.forEach(j -> {

            if (j.getCommitMetadata().isPresent()) {
                CommitMetadata commitMetadata = j.getCommitMetadata().get();

                // Use stream to find the matching majorId and get the version
                Long version = commitIdToVersion.getOrDefault(commitMetadata.getId().getMajorId(), -1L);

                /* Get class name */
                String type = j.getClass().getSimpleName();

                /* Map only certain types and skip certain types for simplification */
                if ("TerminalValueChange".equals(type) || "InitialValueChange".equals(type))
                    return;
                AuditMapped mapped = AuditMapped.builder()
                        .commitId(BigInteger.valueOf(commitMetadata.getId().getMajorId()))
                        .commitDate(commitMetadata.getCommitDate())
                        .id(Integer.valueOf(j.getAffectedLocalId().toString()))
                        .author(commitMetadata.getAuthor())
                        .type(type)
                        .version(version)
                        .build();

                /* Add additional info for field changes */
                if ("ValueChange".equals(type)) {
                    PropertyChange<?> change = (PropertyChange<?>) j;
                    mapped.setOldValue(change.getLeft().toString());
                    mapped.setNewValue(change.getRight().toString());
                    mapped.setFieldName(change.getPropertyName());
                }
                mappedList.add(mapped);
            }
        });
        auditMappedRepository.saveAll(mappedList);
        return mappedList;
    }

    private void exportAsText(List<AuditMapped> records) {
        // Create an ASCII table
        AsciiTable table = new AsciiTable();
        table.getRenderer().setCWC(new CWC_LongestWord());
        table.addRule();
        table.addRow("commit", "id", "commitId", "commitDate",
                "version", "author", "type",
                "fieldName", "oldValue", "newValue");
        table.addRule();

        // Add rows to the table
        for (AuditMapped record : records) {
            table.addRow(
                    record.getCommit() != null ? record.getCommit() : "",
                    record.getId() != null ? record.getId() : "",
                    record.getCommitId() != null ? record.getCommitId() : "",
                    record.getCommitDate() != null ? record.getCommitDate() : "",
                    record.getVersion() != null ? record.getVersion() : "",
                    record.getAuthor() != null ? record.getAuthor() : "",
                    record.getType() != null ? record.getType() : "",
                    record.getFieldName() != null ? record.getFieldName() : "",
                    record.getOldValue() != null ? record.getOldValue() : "",
                    record.getNewValue() != null ? record.getNewValue() : ""
            );
            table.addRule();
        }

        // Render the table
        String tableString = table.render();
        log.info(tableString);
        System.out.println(System.getProperty("user.home"));
        // Write the table to a text file
       String path = "C:/Users/NORUL/Documents/GitHub/tutorials-data-audit/src/main/resources/data.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(tableString);
            log.info("File successfully written to: " + path);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
        log.info("Saving to text file");
    }

}
