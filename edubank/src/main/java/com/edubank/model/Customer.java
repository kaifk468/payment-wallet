package com.edubank.model;
 
import lombok.Data;

import java.time.LocalDate;

/**
 *  This is a bean class. Also called as model class has the attributes to keep customer properties
 *

 * @author ETA_JAVA
 *
 **/
@Data
public class Customer {
	
	private Integer customerId;
	private String emailId;
	private String name;
	private LocalDate dateOfBirth;
	private String password;
	

}
