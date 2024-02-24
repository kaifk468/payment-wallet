package com.amigowallet.dao;

import com.amigowallet.model.UserDto;

/**
 *  This Interface contains the methods responsible for interacting the database
 *  with respect to Login module like getUserByEmailId, change user password, getUserByUserId.
 * 
 *  @author ETA_JAVA
 */

public interface UserLoginDAO {
	
	/**
	 * This method is used to get a UserLogin model corresponding to its
	 * emailId<br>
	 * @param emailId
	 * 
	 * @return User
	 * @throws Exception 
	 */

	public UserDto getUserByEmailId(String emailId) throws Exception;
	
	/**
	 * This method is used to change the password of an existing Customer. It
	 * takes CustomerLogin as a parameter, which includes, customerId and
	 * newPassword, it fetches an entity on the basis of customerId, and updates
	 * the password to newPassword received.<br>
	 * @param userDto
	 */
	public void changeUserPassword(UserDto userDto);

	/**
	 * this method takes the userId as a argument and get the user from the database
	 * 
	 * @param userId
	 * 
	 * @return user
	 */
	public UserDto getUserByUserId(Integer userId);

}
