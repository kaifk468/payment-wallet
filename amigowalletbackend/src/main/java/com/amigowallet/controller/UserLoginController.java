package com.amigowallet.controller;

import com.amigowallet.model.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amigowallet.service.UserLoginService;
@CrossOrigin
@RestController
@RequestMapping("user-login")
public class UserLoginController
{
	@Autowired
	private Environment environment;
	
	@Autowired
	UserLoginService loginService;
	
	static Logger logger = LogManager.getLogger(UserLoginController.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * UserLoginService method to authenticate the user details. <br>
	 * If authentication is success then it sends the user bean
	 * populated with all the required properties like 
	 * cards,transactions,balance,redeem points etc<br>
	 * If verification fails then it sends failure message to the client.
	 * It is populated with an error message,if any error occurs
	 */
	@RequestMapping(value = "authenticate", method = RequestMethod.POST)
	public ResponseEntity<UserDto> authenticate(@RequestBody UserDto userDto) throws Exception {
		try {
			logger.info("USER TRYING TO LOGIN, VALIDATING CREDENTIALS. USER EMAIL : "+ userDto.getEmailId());
			
			userDto = loginService.authenticate(userDto);
			
			logger.info("USER LOGIN SUCCESS, USER EMAIL : "+ userDto.getEmailId());
			
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} 
		catch (Exception e) {

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}

	/**
	 * This method receives the user model having the userId in POST request 
	 * and calls UserLoginService method to retrieve the user details. <br>
	 * If a matching user is found it sends the user bean
	 * populated with all the required properties like 
	 * cards,transactions,balance,redeem points etc<br>
	 * If any error occurs it sends a failure message to the client.
	 */
	@RequestMapping(value = "user", method = RequestMethod.POST)
	public ResponseEntity<UserDto> getUser(@RequestBody UserDto userDto)
	{
		try {
			UserDto userDto1 = loginService.getUserbyUserId(userDto.getUserId());
			
			return new ResponseEntity<UserDto>(userDto1, HttpStatus.OK);
		} 
		catch (Exception e) {
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, environment.getProperty(e.getMessage()));
		}
	}

	/**
	 * This method is used to change the password of logged in customer, if
	 * takes password, new password and confirm new password from the form and,
	 * fetches the customerId are proper then it sets the customerId from the
	 * httpSession. If the entered data is valid, it updates the password,
	 * clears the httpSession data, and redirects to login page. Note: It can
	 * accept only post request
	 */
	@RequestMapping(value = { "/customer-password" }, method = RequestMethod.POST)
	public ResponseEntity<String> customerChangePassword(@RequestBody UserDto userDto) {
		String message = null;
		try {
			logger.info("USER TRYING TO CHANGE PASSWORD, USER ID : "+ userDto.getUserId());
			loginService.changeUserPassword(userDto);
			
			logger.info("PASSWORD CHANGED SUCCESSFULLY FOR, USER ID : "+ userDto.getUserId());

			message = environment.getProperty("UserLoginAPI.PASSWORD_CHANGE_SUCCESS");						
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} 
		catch (Exception e) {
		
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, environment.getProperty(e.getMessage()));
		}
	}
}
