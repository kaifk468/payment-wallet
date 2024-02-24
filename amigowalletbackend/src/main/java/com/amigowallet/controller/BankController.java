package com.amigowallet.controller;

import com.amigowallet.model.*;
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
import com.amigowallet.service.UserTransactionService;

/**
 * This class has methods to handle requests related to Debit Card.
 *
 * @author ETA_JAVA
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("debit-card")
public class BankController {
	
	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	
	@Autowired
	UserTransactionService userTransactionService;
	
	static Logger logger = LogManager.getLogger(BankController.class.getName());
	

   //Load money from bank to the wallet
	@RequestMapping(value="load-money", method = RequestMethod.POST)
	public ResponseEntity<UserTransactionDto> loadMoneyFromBank(@RequestBody MoneyRequestDto moneyRequestDto){

		logger.info("USER TRYING TO LOAD MONEY FROM BANK USING DEBIT CARD, USER ID : "+ moneyRequestDto.getUserId());

		UserTransactionDto userTrasaction = userTransactionService.loadMoneyFromBank(moneyRequestDto);

		logger.info("MONEY LOADED FROM BANK USING DEBIT CARD, USER ID : "+ moneyRequestDto.getUserId());

		return new ResponseEntity<UserTransactionDto>(userTrasaction, HttpStatus.ACCEPTED);

	}


	//transfer money to bank account
	@RequestMapping(value = "send-money-bank", method = RequestMethod.POST)
	public ResponseEntity<UserTransactionDto> sendMoneyToBank(
			@RequestBody MoneyRequestDto moneyRequestDto) {

		logger.info("USER TRYING TO SEND MONEY TO BANK ACCOUNT, USER ID : "
				+ moneyRequestDto.getUserId());

		UserTransactionDto userTrasaction = userTransactionService.sendMoneyToBank(moneyRequestDto);

		logger.info("MONEY SENT TO BANK ACCOUNT, USER ID : " + moneyRequestDto.getUserId());

		return new ResponseEntity<UserTransactionDto>(userTrasaction,
				HttpStatus.ACCEPTED);

	}
	

}
