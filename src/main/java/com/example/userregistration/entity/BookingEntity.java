package com.example.userregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author norulshahlam.mohsen
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Data
@Table(name = "BOOKING")
@JsonIgnoreProperties
public class BookingEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @Column(name = "SI_NO")
    private String siNo;

    @Column(name = "BKG_RQST_STATUS_CODE")
    private String bkgRqstsStatusCode;

    @Column(name = "BKG_OFFICE_CODE")
    private String bkgOfficeCode;

    @Column(name = "HANDLE_OFFICE_CODE")
    private String handleOfficeCode;

    @Column(name = "VSL_CODE")
    private String vslCode;

    @Column(name = "SKD_VOY_NO")
    private String skdVoyNo;

    @Column(name = "SKD_DIR_CODE")
    private String skdDirCode;

    @Column(name = "VSL_NAME")
    private String vslName;

    @Column(name = "POR_CODE"/*, nullable = false*/)
    private String porCode;

    @Column(name = "POR_NAME"/*, nullable = false*/)
    private String porName;

    @Column(name = "POL_CODE"/*, nullable = false*/)
    private String polCode;

    @Column(name = "POL_NAME"/*, nullable = false*/)
    private String polName;

    @Column(name = "POD_CODE"/*, nullable = false*/)
    private String podCode;

    @Column(name = "POD_NAME"/*, nullable = false*/)
    private String podName;

    @Column(name = "DEL_CODE"/*, nullable = false*/)
    private String delCode;

    @Column(name = "DEL_NAME"/*, nullable = false*/)
    private String delName;

    @Column(name = "PKG_QTY")
    private Double pkgQty;

    @Column(name = "PKG_TYPE_CODE")
    private String pkgTypeCode;

    @Column(name = "MEAS_QTY"/*, nullable = false*/)
    private Long measQty;

    @Column(name = "MEAS_UNIT_CODE"/*, nullable = false*/)
    private String measUnitCode;

    @Column(name = "NET_WGT"/*, nullable = false*/)
    private Long netWgt;

    @Column(name = "NET_WGT_UNIT_CODE"/*, nullable = false*/)
    private String netWgtUnitCode;

    @Column(name = "NET_MEAS_QTY"/*, nullable = false*/)
    private Long netMeasQty;

    @Column(name = "NET_MEAS_UNIT_CODE"/*, nullable = false*/)
    private String netMeasUnitCode;

    @Column(name = "DC_FLG")
    private String dcFlg;

    @Column(name = "RC_FLG"/*, nullable = false*/)
    private String rcFlg;

    @Column(name = "AWK_CGO_FLG"/*, nullable = false*/)
    private String awkCgoFlg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RQST_DATE")
    private Date rqstDate;

    @Column(name = "RQST_DELT_FLG"/*, nullable = false*/)
    private String rqstDeltFlg;

    @Column(name = "BKG_UPLD_STATUS_CODE")
    private String bkgUpldStatusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RQST_DEP_DATE"/*, nullable = false*/)
    private Date rqstDepDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RQST_ARR_DATE")
    private Date rqstArrDate;

    @Column(name = "BKG_RMKS1")
    private String bkgRmks1;

    @Column(name = "BKG_RMKS2")
    private String bkgRmks2;

    @Column(name = "AUTO_CFM_EDI_FLG"/*, nullable = false*/)
    private String autoCfmEdiFlg;

    @Column(name = "AUTO_EMAIL_FLG"/*, nullable = false*/)
    private String autoEmailFlg;

    @Column(name = "GDS_DESC")
    private String gdsDesc;

    @Column(name = "MK_DESC")
    private String mkDesc;

    @Column(name = "RQST_ACPT_USR_NAME")
    private String rqstAcptUsrId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RQST_ACPT_UPDATED_DATE")
    private Date rqstAcptUpdDate;

    @Column(name = "OB_TRSP_MOD_CODE")
    private String obTrspModCode;

    @Column(name = "IB_TRSP_MOD_CODE")
    private String ibTrspModCode;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "DELETE_FLAG"/*, nullable = false*/)
    private String deleteFlag;

    @Column(name = "BL_NO")
    private String blNo;

    @Column(name = "SI_RQST_STATUS_CODE")
    private String siRqstStatusCode;

    @Column(name = "BL_RDY_TYPE_CODE")
    private String blRdyTpCode;

    @Column(name = "BL_CPY_KNT")
    private Integer blCpyKnt;

    @Column(name = "OBL_RDEM_KNT")
    private Integer oblRdemKnt;

    @Column(name = "BL_RDY_OFFICE_CODE")
    private String blRdyOfficeCode;

    @Column(name = "BL_RDY_USR_ID")
    private String blRdyUsrId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BL_RDY_DATE")
    private Date blRdyDate;

    @Column(name = "DG_REMARKS")
    private String dgRmks;

    @Column(name = "ACT_VOY_NO")
    private String actVoyNo;

    @Column(name = "DOC_TYPE_Code")
    private String docTpCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TRANSIT_TIME")
    private String transitTime;

    @Column(name = "VOY_CODE")
    private String voyageCode;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "GATE_IN_CUTOFF_DATE")
    private Date gateInCutoffDate;

    @Column(name = "VGM_CUTOFF_DATE")
    private Date vgmCutoffDate;

    @Column(name = "DOCUMENTATION_CUTOFF_DATE")
    private Date documentationCutoffDate;
    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ContractEntity> contracts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ContactEntity> contacts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommodityEntity> commodities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CustomerEntity> customers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EquipmentsEntity> equipments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DangerousGoodsEntity> dangerousGoods = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<VesselVoyageEntity> vesselVoyages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookingEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RefeerCargoEntity> refeerCargos = new ArrayList<>();
}
