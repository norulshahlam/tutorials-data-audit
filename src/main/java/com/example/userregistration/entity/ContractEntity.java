package com.example.userregistration.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Data
@Table(name = "CONTRACT")
public class ContractEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            format = "string",
            example = "AQSDQW-111",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "FRT_TERM_CODE")
    private String frtTermCode;

    @Column(name = "STATUS")
    @Schema(
            format = "string",
            example = "DRAFT",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

}
