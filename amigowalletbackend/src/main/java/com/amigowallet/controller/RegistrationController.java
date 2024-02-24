package com.amigowallet.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.amigowallet.model.SecurityQuestionDto;
import com.amigowallet.service.RegistrationService;

@CrossOrigin
@RestController
@RequestMapping("registration")
public class RegistrationController {

	@Autowired
	private Environment environment;
	
	@Autowired
	RegistrationService registrationService;
	
	static Logger logger = LogManager.getLogger(RegistrationController.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * RegistrationService method to verify the user details. <br>
	 * If verification is success then it sends OTP in an email
	 * to the email id of the user received in POST request. 
	 * Then it sends success message to the client.<br>
	 * If verification fails then it sends failure message to the client.
	 */
	@RequestMapping(value = "validate-registration", method = RequestMethod.POST)
	public ResponseEntity<UserDto> validateForRegistration(@RequestBody UserDto userDto) {
		ResponseEntity<UserDto> responseEntity=null;
		try{
			logger.info("user tring to register : "+
						userDto.getName()+", EMAIL ID : "+ userDto.getEmailId());
			
			registrationService.validateUser(userDto);
			userDto.setSuccessMessage(environment.getProperty("RegistrationAPI.SUCCESSFULLY_VALIDATED"));
			responseEntity=new ResponseEntity<UserDto>(userDto, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			
			if(e.getMessage().contains("Validator")){
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, environment.getProperty(e.getMessage()));
			}
			throw new ResponseStatusException(HttpStatus.CONFLICT, environment.getProperty(e.getMessage()));
		}
		return responseEntity;
	}
	
	@RequestMapping(value="get-questions",method=RequestMethod.GET)
	public ResponseEntity<List<SecurityQuestionDto>> getAllQuestions(){
		ResponseEntity<List<SecurityQuestionDto>> responseEntity=null;
		ArrayList<SecurityQuestionDto> securityQuestionDtos = new ArrayList<>();
			logger.info("Getting all the security questions");

			/*
			 * The following code again validates the user details
			 */
			securityQuestionDtos = registrationService.getAllSecurityQuestions();
			
			responseEntity = new ResponseEntity<List<SecurityQuestionDto>>(securityQuestionDtos, HttpStatus.OK);
			
		return responseEntity;
	}
	
	
	/**
	 * This method receives the user model in POST request and calls
	 * RegistrationService method to validate the user details. <br>
	 * It also verifies the OTP<br>
	 * If verification is success then it calls the registerUser method which
	 * adds the user details into the database 
	 * Then it sends success message along with the
	 * registrationId to the client.<br>
	 * If verification fails then it sends failure message to the client.
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<String> userRegistration(@RequestBody UserDto userDto)
	{
		ResponseEntity<String> responseEntity=null;
		String message=null;
		try {
			registrationService.validateUser(userDto);
			Integer registrationId = registrationService.registerUser(userDto);

			logger.info("USER REGISTERED SUCCESSFULLY, USER EMAIL : "+ userDto.getEmailId());
			message = environment.getProperty("RegistrationAPI.SUCCESSFUL_REGISTRATION")+ registrationId;
			
			responseEntity=new ResponseEntity<String>(message, HttpStatus.CREATED);
		} 
		catch (Exception e){
			if(e.getMessage().contains("Validator")){
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, environment.getProperty(e.getMessage()));
			}
			throw new ResponseStatusException(HttpStatus.CONFLICT, environment.getProperty(e.getMessage()));
		}
		return responseEntity;
	}
}
