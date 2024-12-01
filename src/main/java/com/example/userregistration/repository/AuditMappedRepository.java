package com.example.userregistration.repository;

import com.example.userregistration.entity.AuditMapped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditMappedRepository extends JpaRepository<AuditMapped, Integer> {

}
