package com.example.userregistration.utils;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class Helper {

    public Helper(AuditMappedRepository auditMappedRepository) {
        this.auditMappedRepository = auditMappedRepository;
    }

    private final AuditMappedRepository auditMappedRepository;


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
        log.info("Saving to text file");
        String path = "C:/Users/NORUL/Documents/GitHub/tutorials-data-audit/src/main/resources/data.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(tableString);
            log.info("File successfully written to: " + path);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
        return tableString;
    }


}
