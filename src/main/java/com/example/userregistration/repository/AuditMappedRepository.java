package com.example.userregistration.repository;

import com.example.userregistration.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditMappedRepository extends JpaRepository<AuditLog, Integer> {

}
