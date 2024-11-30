package com.example.userregistration.repository;

import com.example.userregistration.entity.AuditMapped;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@JaversSpringDataAuditable
public interface AuditMappedRepository extends JpaRepository<AuditMapped, Integer> {

}
