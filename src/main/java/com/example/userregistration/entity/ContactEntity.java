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
@Schema(description = "Contact entity")
public class ContactEntity implements Serializable {

    public static final long serialVersionId = 1L;

    @Id
    @Column(name = "ID")
    @Schema(description = "Unique identifier of the contact", example = "1")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DiffExclude
    private Long id;

    @Schema(description = "Name of the contact",
            format = "string",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "NAME")
    private String name;

    @Schema(description = "Email of the contact",
            format = "email"
           )
    @Column(name = "EMAIL")
    @Email
    private String email;

    @Schema(description = "Mobile number of the contact",
            format = "string",
            example = "+65-92212152",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "MOBILE_NO")
    private String mobileNo;
}
