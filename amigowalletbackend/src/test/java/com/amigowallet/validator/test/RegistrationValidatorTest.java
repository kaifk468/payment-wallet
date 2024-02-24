package com.amigowallet.validator.test;

import com.amigowallet.model.UserDto;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.amigowallet.validator.RegistrationValidator;

/**
 * This is a JUnit test class to test the methods of {@link RegistrationValidator}. <br>
 * It covers both positive and negative test cases.
 *
 * @author ETA_JAVA
 *
 */
public class RegistrationValidatorTest
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void validateUserDetailsValid() throws Exception
	{
		 UserDto userDto =new UserDto();
		 userDto.setEmailId("Joseph@infy.com");
		 userDto.setName("Joseph");
		 userDto.setPassword("Joseph#123");
		 userDto.setMobileNumber("9697074340");
		 
		 RegistrationValidator.validateUserDetails(userDto);
		 
		 Assert.assertTrue(true);
	}

	@Test
	public void validateUserDetailsInvalidEmailId() throws Exception
	{
		UserDto userDto =new UserDto();
		userDto.setEmailId("Joseph@infy");
		userDto.setName("Joseph");
		userDto.setPassword("Joseph#123");
		userDto.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_EMAIL");
		RegistrationValidator.validateUserDetails(userDto);
	}

	@Test
	public void validateUserDetailsInvalidName() throws Exception
	{
		UserDto userDto =new UserDto();
		userDto.setEmailId("Joseph@infy.com");
		userDto.setName("Joseph7");
		userDto.setPassword("Joseph#123");
		userDto.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_USER_NAME");
		RegistrationValidator.validateUserDetails(userDto);
	}
	
	@Test
	public void validateUserDetailsInvalidMobileNumber() throws Exception
	{
		UserDto userDto =new UserDto();
		userDto.setEmailId("Joseph@infy.com");
		userDto.setName("Joseph");
		userDto.setPassword("Joseph#123");
		userDto.setMobileNumber("23365");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_CONTACT");
		RegistrationValidator.validateUserDetails(userDto);
	}
	
	@Test
	public void validateUserDetailsInvalidPassword() throws Exception
	{
		UserDto userDto =new UserDto();
		userDto.setEmailId("Joseph@infy.com");
		userDto.setName("Joseph");
		userDto.setPassword("Joseph123");
		userDto.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_PASSWORD");
		RegistrationValidator.validateUserDetails(userDto);
	}

}
