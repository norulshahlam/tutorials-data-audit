package com.example.userregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Data
@Table(name = "CONTACT")
public class ContactEntity extends AuditEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Schema(
            format = "string",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "NAME")
    private String name;


    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NO")
    private String phoneNo;
    @Schema(
            format = "string",
            example = "+65-92212152",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    @JsonIgnore
    private BookingEntity bookingEntity;
}
