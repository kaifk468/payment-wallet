package com.amigowallet.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.amigowallet.model.SecurityQuestionDto;
import com.amigowallet.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.SecurityQuestion;
import com.amigowallet.entity.User;
import com.amigowallet.model.UserStatusDto;

/**
 * This is a DAO class having methods to perform CRUD operations on user and OTP tables.
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value = "registrationDao")
public class RegistrationDAOImpl implements RegistrationDAO{
	
	/** This is a spring auto-wired attribute used to create data base sessions*/
	@Autowired
	EntityManager entityManager;
	
	/**
	 * This method is used to add a new User to the database<br>
	 * It uses session.save() method to save the entity to the database
	 * 
	 * @param userDto
	 * 
	 * @return userId
	 */
	@Override
	public Integer addNewUser(UserDto userDto) {
		
		/*
		 * The properties of userEntity is set by getting the corresponding
		 * properties of the user bean 
		 */		
		User userEntity = new User();
		userEntity.setName(userDto.getName());
		userEntity.setPassword(userDto.getPassword());
		userEntity.setEmailId(userDto.getEmailId());
		userEntity.setMobileNumber(userDto.getMobileNumber());
		userEntity.setSecurityAnswer(userDto.getSecurityAnswer());
	
		SecurityQuestion securityQuestion = entityManager.find(SecurityQuestion.class, userDto.getSecurityQuestion().getQuestionId());
		
		if(securityQuestion !=null) {
			userEntity.setSecurityQuestion(securityQuestion);
		}
		/*
		 * The active status is set to the user Entity
		 */
		userEntity.setUserStatus(UserStatusDto.ACTIVE);
		
		/*
		 * userId is generated by saving the userEntity to the database
		 */
		entityManager.persist(userEntity);
		Integer userId = userEntity.getUserId();
		return userId;	
	}
	
	/**
	 * This method is used for checking whether the email id
	 * is used by an already registered user
	 * 
	 * @param email id
	 * 
	 * @return boolean
	 */
	@Override
	public Boolean checkEmailAvailability(String emailId) {
		
		Query query = entityManager.createQuery("select count(userId) from User where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		Long value = (Long) query.getSingleResult();
		if(value==0){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * This method is used for checking whether the mobile number
	 * is used by an already registered user
	 * 
	 * @param mobileNumber
	 * 
	 * @return boolean
	 */
	@Override
	public Boolean checkMobileNumberAvailability(String mobileNumber) {
		
		Query query = entityManager.createQuery("select count(userId) from User where mobileNumber=:mobileNumber");
		query.setParameter("mobileNumber", mobileNumber);
		
		Long value = (Long) query.getSingleResult();	
		if(value==0){
			return false;
		}else {
			return true;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<SecurityQuestionDto> getAllSecurityQuestions() {
		
		List<SecurityQuestion> securityQuestionEntities = entityManager.createQuery("from SecurityQuestion").getResultList();
		
		if(securityQuestionEntities==null){
			return null;
		}else {
			ArrayList<SecurityQuestionDto> securityQuestionDtos = new ArrayList<>();
			for(SecurityQuestion securityQuestionEntity: securityQuestionEntities) {
				SecurityQuestionDto securityQuestionDto = new SecurityQuestionDto();
				securityQuestionDto.setQuestionId(securityQuestionEntity.getQuestionId());
				securityQuestionDto.setQuestion(securityQuestionEntity.getQuestion());
				securityQuestionDtos.add(securityQuestionDto);
			}
			return securityQuestionDtos;
		}
	}
	
}
