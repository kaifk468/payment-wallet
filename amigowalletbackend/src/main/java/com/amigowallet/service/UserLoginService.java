package com.amigowallet.service;

import com.amigowallet.model.UserDto;


public interface UserLoginService {
	

	public UserDto authenticate(UserDto userDto) throws Exception;

	public void changeUserPassword(UserDto userDto)
			throws Exception;


	public UserDto getUserbyUserId(Integer userId)
			throws Exception;

}
