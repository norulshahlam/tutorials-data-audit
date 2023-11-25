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

@Table(name = "DANGEROUS_GOODS")
public class DangerousGoodsEntity extends AuditEntity implements Serializable {

  public static final long serialVersionId = 1L;

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "DG_GO_ID")
  private Long dgGoId;

  @Column(name = "DG_GO_SEQ")
  private Integer dgGoSeq;

  @Column(name = "BKG_RQST_SEQ")
  private Integer bkgRqstSeq;

  @Column(name = "CNTR_TPSZ_CODE")
  private String cntrTpszCode;

  @Column(name = "CNTR_NO")
  private String cntrNo;

  @Column(name = "IMDG_UN_NO")
  private String imdgUnNo;

  @Column(name = "IMDG_UN_SEQ")
  private Long imdgUnSeq;

  @Column(name = "IMDG_CLSS_ID")
  private String imdgClssId;

  @Column(name = "IMDG_PG_NO")
  private String imdgPgNo;

  @Column(name = "PKG_GRP_CODE1")
  private String pkgGrpCode1;

  @Column(name = "TECHNICAL_NAME")
  private String technicalName;

  @Column(name = "GRS_WGT")
  private Long grsWgt;

  @Column(name = "NET_WGT")
  private Long netWgt;

  @Column(name = "PRP_SHP_NAME")
  private String prpShpName;

  @Column(name = "HZD_CTNT")
  private String hzdCtnt;

  @Column(name = "FLSH_PNT_CTNT")
  private String flshPntCtnt;

  @Column(name = "FLSH_PNT_TEMP_UNIT_CODE")
  private String flshPntTempUnitCode;

  @Column(name = "MRN_POLUT_FLG")
  private String mrnPolutFlg;

  @Column(name = "EMER_CNTC_PNT_CTNT")
  private String emerCntcPntCtnt;

  @Column(name = "DCGO_STATUS_CODE")
  private String dcgoStatusCode;

  @Column(name = "DG_LMT_QTY_FLG")
  private String dgLmtQtyFlg;

  @Column(name = "DCGO_RMK")
  private String dcgoRmk;

  @Column(name = "DG_CNTR_SEQ")
  private Long dgCntrSeq;

  @Column(name = "CNTR_CGO_SEQ")
  private Long cntrCgoSeq;

  @Column(name = "EMER_CNTC_PHN_NO_CTNT")
  private String emerCntcPhnNoCtnt;

  @Column(name = "DG_NET_WGT_UNIT_CODE")
  private String dgNetWgtUnitCode;

  @Column(name = "EMER_CNTC_EMAIL")
  private String emerCntcEmail;

  @Column(name = "OUTR_PKG_CODE")
  private String outrPkgCode;

  @Column(name = "OUTR_PKG_DESC")
  private String outrPkgDesc;

  @Column(name = "OUTR_PKG_QTY")
  private Long outrPkgQty;

  @Column(name = "INR_PKG_CODE")
  private String inrPkgCode;

  @Column(name = "INR_PKG_DESC")
  private String inrPkgDesc;

  @Column(name = "INR_PKG_QTY")
  private Long inrPkgQty;

  @Column(name = "DELETE_FLG")
  private String deleteFlg;

  @Column(name = "STATUS")
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKING_ID")
  @JsonIgnore
  private BookingEntity bookingEntity;
}
