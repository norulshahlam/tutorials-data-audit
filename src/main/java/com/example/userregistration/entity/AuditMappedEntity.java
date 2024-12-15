package com.example.userregistration.entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "AUDIT_MAPPED")
@JsonPropertyOrder({"commit","commitId","version"})
public class AuditMappedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commit;
    private Integer id;
    private BigDecimal commitId;
    private LocalDateTime commitDate;
    private Long version;
    private String author;
    private String type;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String entity;
}
