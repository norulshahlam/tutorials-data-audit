package com.example.userregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Audited
@Data
@Table(name = "CONTRACT")
public class ContractEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Schema(
            format = "string",
            example = "AQSDQW-111",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "FRT_TERM_CODE")
    private String frtTermCode;

    @Column(name = "SCAC_CODE")
    private String scacCode;

    @Column(name = "USA_CSTMS_FILE_NO")
    private String usaCstmsFileNo;

    @Column(name = "CND_CSTMS_FILE_CODE")
    private String cndCstmsFileCode;

    @Column(name = "SC_NO")
    private String scNo;

    @Column(name = "RFA_NO")
    private String rfaNo;

    @Column(name = "IS_RFA"/*, nullable = false*/)
    /*@ColumnDefault("Y")*/
    private String isRfa;

    @Column(name = "STATUS")
    @Schema(
            format = "string",
            example = "DRAFT",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    @JsonIgnore
    private BookingEntity bookingEntity;
}
