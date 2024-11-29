package com.example.userregistration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudiMapped {

    private BigInteger commitId;
    private String version;
    private Integer id;
    private String author;
    private String type;
    private String oldValue;
    private String newValue;
    private Date commitDate;

}
