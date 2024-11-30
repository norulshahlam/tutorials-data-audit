package com.example.userregistration.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "AUDIT_MAPPED")
public class AuditMapped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commit;
    private Integer id;
    private BigInteger commitId;
    private LocalDateTime commitDate;
    private String version;
    private String author;
    private String type;
    private String fieldName;
    private String oldValue;
    private String newValue;

}
