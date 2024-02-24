package com.amigowallet.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.amigowallet.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.User;
import com.amigowallet.model.SecurityQuestionDto;

/**
 * This is a DAO class having methods to perform CRUD operations on user reset password and user tables.
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value="forgotPasswordDAO")
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO{
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;
	
	/**
	 * 
	 * This method receives email id as argument and verifies it. <br>
	 * If verification is success it returns "Found" else it returns "Not Found"
	 * 
	 * @param emailId
	 * 
	 * @return String
	 */
	@Override
	@SuppressWarnings("unchecked")
	public UserDto authenticateEmailId(String emailId) throws Exception
	{		
		Query query = entityManager.createQuery("from User where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		List<User> userEntities = query.getResultList();
		
		/*
		 * If listOfUserEntities is not empty it will return
		 * Found and if it is empty it will return Not Found
		 */
		if(!userEntities.isEmpty()){
			User userEntity = userEntities.get(0);
			UserDto userDto = new UserDto();
			userDto.setUserId(userEntity.getUserId());
			if(userEntity.getSecurityQuestion()!=null){
				SecurityQuestionDto securityQuestionDto = new SecurityQuestionDto();
				securityQuestionDto.setQuestionId(userEntity.getSecurityQuestion().getQuestionId());
				securityQuestionDto.setQuestion(userEntity.getSecurityQuestion().getQuestion());
				userDto.setSecurityQuestion(securityQuestionDto);
			}
			return userDto;
		}
		else
			return null;
	}
	
	/**
	 * 
	 * This method receives email id and hashed password as arguments and
	 * updates the password of the customer to whom the passed email id belongs
	 * 
	 * @param emailId
	 * @param hashedPassword
	 */
	@Override
	public void resetPassword(Integer userId, String hashedPassword) {
		
		 /* uniqueResult: is used to execute the query and get 
		  * single entity instead of list of entities
		  */
		User user = entityManager.find(User.class, userId);
		
		user.setPassword(hashedPassword);
	}
	
	@Override
	public UserDto validateSecurityAnswer(UserDto userDto) {
		
		User userEntity = entityManager.find(User.class, userDto.getUserId());
		
		/*
		 * If listOfUserEntities is not empty it will return
		 * Found and if it is empty it will return Not Found
		 */
		if(userEntity!=null){
			UserDto userDtoFromDB = new UserDto();
			userDtoFromDB.setUserId(userEntity.getUserId());
			userDtoFromDB.setSecurityAnswer(userEntity.getSecurityAnswer());
			return userDtoFromDB;
		}
		else {
			return null;
		}
	}
}
