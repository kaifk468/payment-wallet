package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.amigowallet.model.SecurityQuestionDto;
import com.amigowallet.model.UserDto;


public interface RegistrationService {
	

	public void validateUser(UserDto userDto) throws Exception;

	public Integer registerUser(UserDto userDto) throws NoSuchAlgorithmException;

	public ArrayList<SecurityQuestionDto> getAllSecurityQuestions();
	
}
