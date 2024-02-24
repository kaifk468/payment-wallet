package com.amigowallet.service.serviceImpl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.amigowallet.model.UserDto;
import com.amigowallet.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.model.SecurityQuestionDto;
import com.amigowallet.utility.HashingUtility;
import com.amigowallet.validator.RegistrationValidator;
@Service(value = "registrationService")
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private RegistrationDAO registrationDao;

	@Override
	public void validateUser(UserDto userDto) throws Exception {

		RegistrationValidator.validateUserDetails(userDto);

		Boolean emailAvailable = registrationDao.checkEmailAvailability(userDto
				.getEmailId());


		if (emailAvailable) {
			throw new Exception(
					"RegistrationService.EMAIL_ALREADY_PRESENT");
		}

		Boolean mobileNumberAvailable = registrationDao
				.checkMobileNumberAvailability(userDto.getMobileNumber());
		if (mobileNumberAvailable) {
			throw new Exception(
					"RegistrationService.MOBILE_NUMBER_ALREADY_PRESENT");
		}
	}

	@Override
	public Integer registerUser(UserDto userDto) throws NoSuchAlgorithmException {

		userDto.setPassword(HashingUtility.getHashValue(userDto.getPassword()));
		
		userDto.setEmailId(userDto.getEmailId().toLowerCase());

		Integer userId = registrationDao.addNewUser(userDto);
		
		return userId;
	}


	@Override
	public ArrayList<SecurityQuestionDto> getAllSecurityQuestions(){

		ArrayList<SecurityQuestionDto> securityQuestionDtos = registrationDao.getAllSecurityQuestions();
		
		return securityQuestionDtos;
	}

}
