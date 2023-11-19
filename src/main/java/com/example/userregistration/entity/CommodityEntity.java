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

@Table(name = "COMMODITY")
public class CommodityEntity extends AuditEntity implements Serializable {

  public static final long serialVersionId = 1L;

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "EST_WGT")
  private Double estWgt;

  @Column(name = "EST_WGT_UNIT_CODE")
  private String estWgtUnitCode;

  @Column(name = "CMDT_CODE")
  private String cmdtyCode;

  @Column(name = "CMDT_DESC")
  private String cmdtyDesc;

  @Column(name = "SOC_FLG")
  private String socFlg;

  @Column(name = "SLT_OWNR_CODE")
  private String sltOwnrCode;

  @Column(name = "CNTR_OPR_CODE")
  private String cntrOprCode;

  @Column(name = "IS_EMPTY")
  private String isEmpty;

  @Column(name = "IASOC_FLG")
  private String iasocFlg;

  @Column(name = "IASOC_VOY_CODE")
  private String iasocVoyCode;

  @Column(name = "IASOC_VSL_NAME")
  private String iasocVslName;

  @Column(name = "STATUS")
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKING_ID")
  private BookingEntity bookingEntity;
}
