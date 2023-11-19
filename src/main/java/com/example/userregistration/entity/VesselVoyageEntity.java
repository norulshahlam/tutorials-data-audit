package com.example.userregistration.entity;

import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "VESSEL_VOYAGE")
public class VesselVoyageEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "VSL_PRE_PST_CODE")
    private String vslPrePstCode;

    @Column(name = "VSL_SEQ")
    private Integer vslSeq;

    @Column(name = "RQST_MST_SEQ")
    private Integer rqstMstSeq;

    @Column(name = "LOCAL_VOY_NO")
    private String localVoyNo;

    @Column(name = "POL_CODE")
    private String polCode;

    @Column(name = "POL_NAME")
    private String polName;

    @Column(name = "POD_CODE")
    private String podCode;

    @Column(name = "ROUTE_TYPE_CODE")
    private String routeTpCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETD_DATE")
    private Date etdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETA_DATE")
    private Date etaDate;

    @Column(name = "VSL_CODE")
    private String vslCode;

    @Column(name = "VSL_NAME")
    private String vslName;

    @Column(name = "VOY_CODE")
    private String voyageCode;

    @Column(name = "DELETE_FLG")
    private String deleteFlg;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    private BookingEntity bookingEntity;
}
