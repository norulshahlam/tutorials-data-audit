package com.example.userregistration.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffExclude;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CONTACT")
public class ContactEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DiffExclude
    private Long id;

    @Schema(
            format = "string",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "NAME")
    private String name;

    @Schema(
            format = "email"
           )
    @Column(name = "EMAIL")
    @Email
    private String email;

    @Schema(
            format = "string",
            example = "+65-92212152",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "MOBILE_NO")
    private String mobileNo;
}
