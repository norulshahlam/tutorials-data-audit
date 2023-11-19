package com.example.userregistration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {

	@Column(name = "CREATED_BY", updatable = false)
	@CreatedBy
	protected String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "CREATED_DATE", updatable = false)
	protected Date createdDate;

	@Column(name = "UPDATED_BY")
	@LastModifiedBy
	protected String updatedBy;

	@Column(name = "UPDATED_DATE")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	protected Date updatedDate;

}
