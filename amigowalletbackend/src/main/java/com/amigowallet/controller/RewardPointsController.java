package com.amigowallet.controller;

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

import com.amigowallet.model.UserDto;
import com.amigowallet.service.RewardPointsService;

@RestController
@CrossOrigin
@RequestMapping("reward-point")
public class RewardPointsController {

	@Autowired
	private Environment environment;
	
	@Autowired
	RewardPointsService rewardPointsService;
	
	static Logger logger = LogManager.getLogger(RewardPointsController.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * RewardPointsService method to redeem the Reward points. <br>
	 *
	 */
	@RequestMapping(value = "redeem-points", method = RequestMethod.POST)
	public ResponseEntity<UserDto> redeemRewardPoints(@RequestBody UserDto userDto) {
		Integer userId= userDto.getUserId();
		ResponseEntity<UserDto> responseEntity = null;
		try {
			logger.info("USER TRYING TO REDEEM REWARD POINTS, USER ID : "+ userDto.getUserId());
			
			rewardPointsService.redeemRewardPoints(userId);
			
			logger.info("ALL REWARD POINTS REDEEMED SUCCESSFULLY, USER ID : "+ userDto.getUserId());
			userDto.setSuccessMessage(environment.getProperty("RewardPointsAPI.REWARD_POINTS_REDEEMED_SUCCESS"));
			responseEntity = new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		} catch (Exception e) {
			
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, environment.getProperty(e.getMessage()));
		}
		
		return responseEntity;
	}

}
