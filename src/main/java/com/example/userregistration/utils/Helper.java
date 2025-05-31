package com.example.userregistration.utils;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class Helper {

    public Helper(AuditMappedRepository auditMappedRepository) {
        this.auditMappedRepository = auditMappedRepository;
    }

    private final AuditMappedRepository auditMappedRepository;

    public ResponseEntity<InputStreamResource> generateAuditReport(List<AuditMappedEntity> records) {
        if (records == null || records.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Create an ASCII table and export the data
        String tableString = exportAsText(records);

        // Convert the content to an InputStream (can be a ByteArrayInputStream)
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tableString.getBytes(StandardCharsets.UTF_8));

        // Create a resource from the byte array
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        // Set the headers and return the file as a response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=audit_report.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    public String exportAsText(List<AuditMappedEntity> records) {
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
                    record.getSessionId() != null ? record.getSessionId() : "",
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
        log.info("Saving to text file");
        String path = "C:/Users/NORUL/Documents/GitHub/tutorials-data-audit/src/main/resources/data.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(tableString);
            log.info("File successfully written to: {}", path);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
        return tableString;
    }


}
