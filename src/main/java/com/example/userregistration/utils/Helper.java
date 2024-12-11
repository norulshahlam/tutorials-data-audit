package com.example.userregistration.utils;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Changes;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Helper {

    public Helper(AuditMappedRepository auditMappedRepository) {
        this.auditMappedRepository = auditMappedRepository;
    }

    private final AuditMappedRepository auditMappedRepository;

    public List<AuditMappedEntity> customizeAuditDetails(Changes changes, List<CdoSnapshot> snapshots) {

        // Map commitId (majorId) to version
        Map<String, Long> commitIdToVersion = snapshots.stream()
                .collect(Collectors.toMap(snapshot -> snapshot.getCommitMetadata().getId().toString(), CdoSnapshot::getVersion));

        List<AuditMappedEntity> mappedList = new ArrayList<>();

        changes.forEach(j -> {

            if (j.getCommitMetadata().isPresent()) {
                CommitMetadata commitMetadata = j.getCommitMetadata().get();
                String entityName = j.getAffectedGlobalId().getTypeName().substring(j.getAffectedGlobalId().getTypeName().lastIndexOf('.') + 1);

                // Use stream to find the matching majorId and get the version
                Long version = commitIdToVersion.getOrDefault(commitMetadata.getId().toString(), -1L);

                /* Get class name */
                String type = j.getClass().getSimpleName();

                /* Map only certain types and skip certain types for simplification */
                if ("TerminalValueChange".equals(type) || "InitialValueChange".equals(type))
                    return;
                AuditMappedEntity mapped = AuditMappedEntity.builder()
                        .commitId(new BigDecimal(commitMetadata.getId().toString()))
                        .commitDate(commitMetadata.getCommitDate())
                        .id(Integer.valueOf(j.getAffectedLocalId().toString()))
                        .author(commitMetadata.getAuthor())
                        .type(type)
                        .entity(entityName)
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

    public void exportAsText(List<AuditMappedEntity> records) {
        // Create an ASCII table
        AsciiTable table = new AsciiTable();
        table.getRenderer().setCWC(new CWC_LongestWord());
        table.addRule();
        table.addRow("commit", "id", "commitId", "commitDate",
                "version", "author", "type",
                "fieldName", "oldValue", "newValue", "entity");
        table.addRule();

        // Add rows to the table
        for (AuditMappedEntity record : records) {
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
                    record.getNewValue() != null ? record.getNewValue() : "",
                    record.getEntity() != null ? record.getEntity() : ""
            );
            table.addRule();
        }

        // Render the table
        String tableString = table.render();

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
