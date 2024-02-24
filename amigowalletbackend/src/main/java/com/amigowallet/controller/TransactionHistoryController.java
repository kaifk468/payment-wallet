package com.amigowallet.controller;

import java.util.List;

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
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.service.TransactionHistoryService;

@RestController
@CrossOrigin
@RequestMapping("transaction-history")
public class TransactionHistoryController {

	@Autowired
	private Environment environment;
	
	@Autowired
	TransactionHistoryService transactionHistoryService;
	
	static Logger logger = LogManager.getLogger(TransactionHistoryController.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * TransactionHistoryService method to fetch transaction history. <br>
	 *
	 */
	@RequestMapping(value = "get-transactions", method = RequestMethod.POST)
	public ResponseEntity<List<UserTransactionDto>> getAllTransactions(@RequestBody UserDto userDto){
		Integer userId= userDto.getUserId();
		ResponseEntity<List<UserTransactionDto>> responseEntity = null;
		try {
			logger.info("USER TRYING TO FETCH TRANSACTION HISTORY, USER ID : "+ userDto.getUserId());
			
			List<UserTransactionDto> userTransactionDtos = transactionHistoryService.getAllTransactionByUserId(userId);
			
			logger.info("FETCHING OF TRANSACTION HISTORY SUCCESSFULLY, USER ID : "+ userDto.getUserId());
			
			responseEntity = new ResponseEntity<List<UserTransactionDto>>(userTransactionDtos, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(e.getMessage()));
		}
		return responseEntity;
	}
	
}
