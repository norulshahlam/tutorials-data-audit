package com.example.userregistration.service;

import com.example.userregistration.entity.AuditMappedEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.repository.AuditMappedRepository;
import com.example.userregistration.repository.ContactRepository;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ContactChangeAspectTest {

    @MockBean
    private ContactRepository contactRepository;

    @MockBean
    private AuditMappedRepository auditMappedRepository;

    @InjectMocks
    private ContactChangeAspect contactChangeAspect;

    private ContactEntity contactEntity;
    private ContactEntity updatedContactEntity;
    private ContactEntity previousState;
    private DiffResult<ContactEntity> diffResult;
    private List<AuditMappedEntity> update;

    @BeforeEach
    void setUp() {
        contactEntity = new ContactEntity();
        contactEntity.setId(1L);
        contactEntity.setEmail("test@example.com");
        contactEntity.setName("Test User");
        contactEntity.setMobileNo("1234567890");

        updatedContactEntity = new ContactEntity();
        updatedContactEntity.setId(1L);
        updatedContactEntity.setEmail("test@example.com");
        updatedContactEntity.setName("Test User");
        updatedContactEntity.setMobileNo("12345678901234567890");

        previousState = new ContactEntity();
        previousState.setId(1L);
        previousState.setEmail("test@example.com");
        previousState.setName("Test User");
        previousState.setMobileNo("1234567890");

        diffResult = new ReflectionDiffBuilder<>(updatedContactEntity, previousState, ToStringStyle.DEFAULT_STYLE).build();

        update = diffResult.getDiffs()
                .stream()
                .map(i -> AuditMappedEntity.builder()
                        .commitDate(LocalDateTime.now())
                        .sessionId(null)
                        .id(Math.toIntExact(updatedContactEntity.getId()))
                        .author("SYSTEM_USERNAME")
                        .type("UPDATE")
                        .fieldName(i.getFieldName())
                        .newValue(i.getLeft().toString())
                        .oldValue(i.getRight().toString())
                        .entity(updatedContactEntity.getClass().getSimpleName())
                        .build()).toList();
    }

    @Test
    void editContactPointcut_shouldCorrectlyHandleContactEntityWithFieldUpdatedToLargeString() {
        // Given
        when(contactRepository.findById(contactEntity.getId())).thenReturn(Optional.of(contactEntity));

        // When
        contactChangeAspect.handleAuditableUpdate(null, null,null);

        // Then
        assertEquals(update.size(), 1);
        assertEquals(update.get(0).getFieldName(), "mobileNo");
        assertEquals(update.get(0).getNewValue(), "12345678901234567890");
        assertEquals(update.get(0).getOldValue(), "1234567890");
    }
}