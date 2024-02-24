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

import com.amigowallet.service.ForgotPasswordService;

/**
 * This class has methods to handle forgot password and reset password requests.
 *
 * @author ETA_JAVA
 *
 */
@CrossOrigin
@RestController
@RequestMapping("forgot-password")
public class ForgotPasswordController {
	
	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	@Autowired
	ForgotPasswordService forgotPasswordService;
	
	static Logger logger = LogManager.getLogger(ForgotPasswordController.class.getName());

	@RequestMapping(value = "forgot-password", method = RequestMethod.POST)
	public ResponseEntity<UserDto> forgotPassword(@RequestBody String emailId) {
		try 
		{
			logger.info("USER FORGOT PASSWORD, VALIDATING EMAIL. USER EMAIL : "+emailId);
			
			UserDto userDto = forgotPasswordService.authenticateEmailId(emailId);
			
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, environment.getProperty(e.getMessage()));
		}
	}

	@RequestMapping(value = "validate-answer", method = RequestMethod.POST)
	public ResponseEntity<String> validateSecurityAnswer(@RequestBody UserDto userDto){
		try 
		{
			logger.info("USER FORGOT PASSWORD, VALIDATING SECURITY ANSWER : "+ userDto.getUserId());
			
			forgotPasswordService.validateSecurityAnswer(userDto);
			
			return new ResponseEntity<String>(environment.getProperty("ForgotPasswordAPI.SUCCESSFULLY_VALIDATED_ANSWER"), HttpStatus.ACCEPTED);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, environment.getProperty(e.getMessage()));
		}
	}
	
	/**
	 * This method is used to reset the password. It receives password reset
	 * token, new password and confirm new password values in POST request. If
	 * the password reset is success it sends success message else it sends
	 * failure message as response to client.
	 */
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@RequestBody UserDto userDto) {
		String message = null;
		try {
			logger.info("RESETNG PASSWORD. FOR User : " + userDto.getUserId());
			forgotPasswordService.resetPassword(userDto);
			logger.info("PASSWORD RESET SUCCESSFULLY . FOR User : " + userDto.getUserId());

			/*
			 * The following code populates a string with a success message
			 */
			message = environment.getProperty("ForgotPasswordAPI.RESET_PASSWORD_SUCCESS");
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, environment.getProperty(e.getMessage()));
		}
	}
}
