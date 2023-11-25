package com.example.userregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "REEFER_CARGO")
public class RefeerCargoEntity extends AuditEntity implements Serializable {
    public static final long serialVersionId = 1L;

    //TODO needs verification, yet to receive DB script

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RG_CARGO_ID")
    private String rfCargoId;

    @Column(name = "RF_CARGO_SEQ")
    private Long rfCargoSeq;

    @Column(name = "BKG_RQST_SEQ")
    private Integer bkgRqstSeq;

    @Column(name = "CNTR_TPSZ_CODE")
    private String cntrTpszCode;

    @Column(name = "CNTR_NO")
    private String cntrNo;

    @Column(name = "MIN_TEMP")
    private Double minTemp;

    @Column(name = "MIN_TEMP_UNIT_CODE")
    private String minTempUnitCode;

    @Column(name = "MAX_TEMP")
    private Double maxTemp;

    @Column(name = "MAX_TEMP_UNIT_CODE")
    private String maxTempUnitCode;

    @Column(name = "HUMID_RTO")
    private Double humidRto;

    @Column(name = "CNTR_VENT_CODE")
    private String cntrVentCode;

    @Column(name = "VENT_RTO")
    private Double ventRto;

    @Column(name = "CLNG_TP_CODE")
    private String clngTpCode;

    @Column(name = "DELETE_FLG")
    private String deleteFlg;

    @Column(name = "CMDTY_CODE")
    private String cmdtyCode;

    @Column(name = "CMDTY_DESC")
    private String cmdtyDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    @JsonIgnore
    private BookingEntity bookingEntity;
}
