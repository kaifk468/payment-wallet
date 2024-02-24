package com.edubank.model;

import lombok.Data;

/**
 *  This is a bean class. Also called as model class has the attributes to keep account properties
 *  
 * @author ETA_JAVA

 **/
@Data
public class Account
{
	private String accountNumber; 
	private String accountHolderName;
	private Double balance;
	private AccountStatus accountStatus;
	private Integer customerId;
	private String ifscCode;
}
