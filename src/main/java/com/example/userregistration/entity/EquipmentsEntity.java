package com.example.userregistration.entity;

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
@Table(name = "EQUIPMENT")
public class EquipmentsEntity extends AuditEntity implements Serializable {

  public static final long serialVersionId = 1L;

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "EQP_QTY_ID")
  private Long eqpQtyId;

  @Column(name = "CNTR_TPSZ_CODE")
  private String cntrTpszCode;

  @Column(name = "CNTR_TPSZ_NAME")
  private String cntrTpszName;

  @Column(name = "BKG_RQST_SEQ")
  private Long bkgRqstSeq;

  @Column(name = "CNTR_QTY")
  private Double cntrQty;

  @Column(name = "FLEX_HGT_FLG")
  private String flexHgtFlg;

  @Column(name = "DELETE_FLG")
  private String deleteFlg;

  @Column(name = "STATUS")
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKING_ID")
  private BookingEntity bookingEntity;

}
