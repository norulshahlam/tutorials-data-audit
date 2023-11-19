package com.example.userregistration.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "CUSTOMER")
public class CustomerEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "BKG_RQST_SEQ")
    private Integer bkgRqstSeq;

    @Column(name = "CUST_ID")
    private Long custId;

    @Column(name = "CUST_TYPE_CODE")
    private String custTpCode;

    @Column(name = "CNT_CODE")
    private String cntCode;

    @Column(name = "CUST_SEQ")
    private Integer custSeq;

    @Column(name = "CUST_NAME")
    private String custName;

    @Column(name = "CUST_ADDR")
    private String custAddr;

    @Column(name = "LOC_CODE")
    private String locCode;

    @Column(name = "LOC_NAME")
    private String locName;

    @Column(name = "LOC_CTNT")
    private String locCtnt;

    @Column(name = "PST_CTNT")
    private String pstCtnt;

    @Column(name = "CUST_EMAIL")
    private String custEmail;

    @Column(name = "CNTC_NAME")
    private String cntcName;

    @Column(name = "CNTC_EMAIL")
    private String cntcEmail;

    @Column(name = "CUST_ERP_REF_NO")
    private String custErpRefNo;

    @Column(name = "EORI_NO")
    private String eoriNo;

    @Column(name = "EUR_CSTMS_ST_NAME")
    private String eurCstmsStName;

    @Column(name = "PRNR_CUST_CODE")
    private String prnrCustCode;

    @Column(name = "DELETE_FLG")
    private String deleteFlg;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    private BookingEntity bookingEntity;




}
