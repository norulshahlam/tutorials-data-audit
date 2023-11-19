package com.example.userregistration.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

/**
 * @author norulshahlam.mohsen
 */
@Entity
@Audited
@Data
public class UserEntity {

    @Id
    @Schema(
            format = "email",
            example = "jane@pil.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = """
                    Email of customer. </b>
                    1. User input </b>
                    2. Check If email id exists in myPIL database </b>
                    3. If exists, then redirect Customer to login screen for existing Customers. </b>
                    4. Validate input format </b>
                    <ul>
                        <li>If Customer is inactive, prompt for password change </li>
                        <li>If password expired, prompt for password change</li>
                    </ul>
                    """)
    @Email
    private String customerEmail;
    @Schema(type = "string",
            example = "my-password",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            description = "Password requirements\n" +
                          "1.\tAt least 12 characters long but 14 or more is better.\n" +
                          "2.\tA combination of uppercase letters, lowercase letters, numbers, and symbols.\n" +
                          "3.\tNot a word that can be found in a dictionary or the name of a person, character, product, or organization.\n" +
                          "4.\tSignificantly different from your previous passwords.\n")
    private String password;
    @Schema(type = "string",
            example = "Shipper",
            description = "Role of customer.\n" +
                          "1.\tMaster data\n" +
                          "2.\tData displayed from myPIL database. \n" +
                          "3.\tCustomer Role has no business impact at this time." +
                          "4.\tOnly below options are displayed, and the role list should be configurable:\n" +
                          "<ul>" +
                          "<li>Shipper</li> " +
                          "<li>FWDR</li>" +
                          "<li>Consignee</li>" +
                          "<li>Handling Agent</li>" +
                          "<li>Notify Party</li>" +
                          "<li>Price Owner</li>" +
                          "<li>Bill To Party" +
                          "</ul>",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerRole;
    @Schema(type = "string",
            example = "China",
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Country of customer. \n" +
                          "1.\tMaster data\n" +
                          "2.\tCustomer can manually input\n" +
                          "3.\tWild card search is applicable.\n" +
                          "4.\tCountry code and name will be displayed on search and selection\n" +
                          "5.\tManual input will be validated against master data.\n" +
                          "6.\tWild card search is enabled.\n" +
                          "7.\tFetched from myPIL database (TBD).\n")
    private String country;
}
