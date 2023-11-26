package com.example.userregistration.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author norulshahlam.mohsen
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "BOOKING")
public class BookingEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            format = "string",
            example = "AUS35-1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "BKG_RQST_NO"/*, nullable = false*/)
    private String bkgRqstNo;


    @Column(name = "BKG_RQST_STATUS_SEQ"/*, nullable = false*/)
    private Integer bkgRqstStatusSeq;

    @Column(name = "BKG_NO"/*, nullable = false*/)
    private String bkgNo;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bookingId", referencedColumnName = "id")
    private List<ContractEntity> contracts = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bookingId", referencedColumnName = "id")
    private List<ContactEntity> contacts = new ArrayList<>();

}
