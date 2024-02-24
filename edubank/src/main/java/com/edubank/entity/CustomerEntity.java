package com.edubank.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This is an entity class mapped to DataBase table <q>CUSTOMER</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="CUSTOMER")
@Data
public class CustomerEntity{
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID")
	private Integer customerId;
	@Column(name="EMAILID")
	private String emailId;
	@Column(name="NAME")
	private String name;
	@Column(name="DATE_OF_BIRTH")
	private LocalDate dateOfBirth;

	@Column(name = "PASSWORD")
	private String password;
}
