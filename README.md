# Data auditing 

## This is a tutorial for auditing manually using AOP

## Swagger url
    http://localhost:8080/swagger-ui/index.html

## Download swagger specifications
    http://localhost:8080/v3/api-docs.yaml

## Access table via Access h2 console

    http://localhost:8080/h2-console  
    JDBC URL: jdbc:h2:mem:audittable  
    SELECT * FROM AUDIT_MAPPED

## Use case

Add contact entity as single or a list in Swagger UI. Then edit or delete them. it will be reflected in the Audit table via h2 console
