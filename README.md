# User Registration module

## This is a tutorial for auditing using [Javers](https://javers.org/documentation/jql-examples/)

### Swagger url
    http://localhost:8080/swagger-ui/index.html

### Download swagger specifications
    http://localhost:8080/v3/api-docs.yaml

### Access table via Access h2 console

    http://localhost:8080/h2-console  
    JDBC URL: jdbc:h2:mem:audittable  
    SELECT * FROM JV_SNAPSHOT  

### How to clean up snapshots and commits after a period of time in Javers?

Clean up can be done in the following order:

    DELETE FROM jv_commit_property;
    DELETE FROM jv_snapshot;
    DELETE FROM jv_commit;
    DELETE FROM jv_global_id WHERE owner_id_fk IS NOT NULL;
    DELETE FROM jv_global_id;
    Note : You can put the filter clause as per your need which is not considered in the above DB Script.