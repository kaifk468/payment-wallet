package com.edubank.entity;

import javax.persistence.*;

import com.edubank.model.AccountStatus;
import lombok.Data;

/**
 * This is an entity class mapped to Database table <q>ACCOUNT</q>
 * 
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="ACCOUNT")
@Data
public class AccountEntity
{
	/**
	 * @Id is used to map primary key of this table
	 * 
	 * @column is used to map a property with a column in table where column name is different
	 * in this case 'accountId' is mapped with ACCOUNT_ID column of this table  
	 * */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ACCOUNT_ID ")
	private Integer accountId;
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name="BALANCE")
	private Double balance;
	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Enumerated(EnumType.STRING)
	@Column(name="ACCOUNT_STATUS")
	private AccountStatus accountStatus;

	@OneToOne
	CustomerEntity customer;

}
