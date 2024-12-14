# Data auditing 

## This is a tutorial for auditing manually using AOP 

There is booking entity that contains contact entity as child entity. Add either one first, then edit or delete using Swagger. 

## Swagger url
    http://localhost:8080/swagger-ui/index.html

## Download swagger specifications
    http://localhost:8080/v3/api-docs.yaml

## Access table via Access h2 console

    http://localhost:8080/h2-console  
    JDBC URL: jdbc:h2:mem:audittable  
    SELECT * FROM AUDIT_MAPPED

## Use case

There will be preloaded data, so you can access the audit right away

    add 2 new records
    edit both records
    delete both records

## NEW - added table for custom audit mapping
Run the endpoint `/audit/contactsPretty` first, and it will save for you your custom audit mapping.

    SELECT commit, commit_id, version, id, author, type, field_name, old_value, new_value, entity, commit_date FROM AUDIT_MAPPED order by commit_date desc
