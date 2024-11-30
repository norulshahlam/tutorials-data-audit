package com.example.userregistration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudiMapped {

    private BigInteger commitId;
    private LocalDateTime commitDate;
    private String version;
    private Integer id;
    private String author;
    private String type;
    private String fieldName;
    private String oldValue;
    private String newValue;

}
