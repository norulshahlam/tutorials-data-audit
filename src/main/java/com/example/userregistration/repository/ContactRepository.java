package com.example.userregistration.repository;

import com.example.userregistration.entity.ContactEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@JaversSpringDataAuditable
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
