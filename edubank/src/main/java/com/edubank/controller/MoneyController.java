package com.edubank.controller;

import com.edubank.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.service.AccountService;
import com.edubank.service.CustomerService;
import com.edubank.service.TransactionService;

/**
 * This controller class has methods to handle requests related to adding money to
 * customer account by teller.
 * 
 * @author ETA_JAVA
 *
 */
@RestController
public class MoneyController {

	@Autowired
	private Environment environment;

	@Autowired
	AccountService accountService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CustomerService customerService;
	
	
	private Long numberOfCustomers;

	static Logger logger = LogManager.getLogger(MoneyController.class);


	@RequestMapping(value = "/addMoneyToBankAccount", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<MoneyResponse> addMoney(@RequestBody RequestWrapper<MoneyRequest>  requestWrapper) throws Exception {
		ResponseWrapper<MoneyResponse> responseWrapper = new ResponseWrapper<>();
		MoneyResponse addMoneyResponse = new MoneyResponse();
		if(!TransactionType.CREDIT.toString().equalsIgnoreCase(requestWrapper.getRequest().getTransactionType()))
		{
			throw new Exception("Invalid Transaction Type");
		}

		try {
			Account account = accountService.getAccountByAccountNumber(requestWrapper.getRequest().getAccountNumber());
			if(!account.getIfscCode().equalsIgnoreCase(requestWrapper.getRequest().getIfscCode()))
			{
				throw new Exception("AddMoneyController.Bank code does't match");
			}
			Integer customerId= account.getCustomerId();
			//Validate Customer is active
			Customer customer=customerService.getCustomerByCustomerId(customerId);
			if(customer==null)
			{
				throw new Exception("AddMoneyController.CUSTOMER_NOT_ACTIVE");
			}
			if(!customer.getPassword().equals(requestWrapper.getRequest().getPassword()))
			{
				throw new Exception("Password does't match");
			}

			Transaction transaction = new Transaction();
			transaction.setAccountNumber(requestWrapper.getRequest().getAccountNumber());
			transaction.setAmount(requestWrapper.getRequest().getAmount());
			transaction.setType(TransactionType.CREDIT);
			transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
			transaction.setTransactionMode("Teller");
			transactionService.addTransaction(transaction, "" + requestWrapper.getRequest().getAccountNumber());
		} catch (Exception exception) {
			//responseWrapper.setErrorMessage(environment.getProperty(exception.getMessage()));
			throw new Exception(exception.getMessage());
		}
		accountService.addMoney(requestWrapper.getRequest().getAccountNumber(), requestWrapper.getRequest().getAmount());
		addMoneyResponse.setMessage(requestWrapper.getRequest().getAmount()+": Money added successfully");
		addMoneyResponse.setTotalBalance(accountService.getAccountByAccountNumber(requestWrapper.getRequest().getAccountNumber()).getBalance());
		responseWrapper.setResponse(addMoneyResponse);
		return responseWrapper;
	}

	@PostMapping(value = "/debitMoneyFromBankAccount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<MoneyResponse> debitMoney(@RequestBody RequestWrapper<MoneyRequest> requestWrapper) throws Exception {
		ResponseWrapper<MoneyResponse> responseWrapper = new ResponseWrapper<>();
		MoneyResponse moneyResponse = new MoneyResponse();
		try {

			if(!TransactionType.DEBIT.toString().equalsIgnoreCase(requestWrapper.getRequest().getTransactionType()))
			{
				throw new Exception("Invalid Transaction Type");
			}
			Account account = accountService.getAccountByAccountNumber(requestWrapper.getRequest().getAccountNumber());
			Integer customerId= account.getCustomerId();
			//Validate Customer is active
			Customer customer=customerService.getCustomerByCustomerId(customerId);
			if(customer==null)
			{
				throw new Exception("AddMoneyController.CUSTOMER_NOT_ACTIVE");
			}
			if(!customer.getPassword().equals(requestWrapper.getRequest().getPassword()))
			{
				throw new Exception("Password does't match");
			}
			Transaction transaction = new Transaction();
			transaction.setAccountNumber(requestWrapper.getRequest().getAccountNumber());
			transaction.setAmount(requestWrapper.getRequest().getAmount());
			transaction.setType(TransactionType.DEBIT);
			transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
			transaction.setTransactionMode("Teller");
			transactionService.addTransaction(transaction, "" + requestWrapper.getRequest().getAccountNumber());
		} catch (Exception exception) {
			//responseWrapper.setErrorMessage(environment.getProperty(exception.getMessage()));
			throw new Exception(exception.getMessage());
		}
		accountService.debitAmount(requestWrapper.getRequest().getAccountNumber(), requestWrapper.getRequest().getAmount());
		moneyResponse.setTotalBalance(accountService.getAccountByAccountNumber(requestWrapper.getRequest().getAccountNumber()).getBalance());
		moneyResponse.setMessage(requestWrapper.getRequest().getAmount()+": Money debited successfully");

		responseWrapper.setResponse(moneyResponse);
		return responseWrapper;
	}

	@RequestMapping(value = { "/customerAddMoney" })
	public ModelAndView getAddMoneyView() {
		ModelAndView model = new ModelAndView("customerAddMoney");
		return model;
	}

	/**
	 * This method is used to add money to the customer account and redirects to
	 * teller's home page with a success message else show proper error message
	 * on the current page Note: It can accept only post request
	 * 
	 * @param accountNumber
	 * @param amount
	 * @param httpSession
	 * 
	 * @return ModelAndView object redirecting to home page<br>
	 */
//	@RequestMapping(value = "addMoney", method = RequestMethod.POST)
//	public ModelAndView addMoney(@RequestParam("accountNumber") String accNo,
//			@RequestParam("amount") Double amt, HttpSession httpSession) {
//		ModelAndView model = new ModelAndView("customerAddMoney");
//		Integer tellerId = null;
//		try
//		{
//			logger.info("ADD MONEY REQUEST... ACCOUNT NUMBER : "+accNo+", AMMOUNT : "+amt+", WHO IS ADDING - TELLER ID : "+httpSession.getAttribute("tellerId"));
//
//			/* httpSession.getAttribute Returns the object bound with the
//			 * specified name in this session, or null if no object is bound
//			 * under the name.
//			 */
//			if (httpSession.getAttribute("tellerId") != null) {
//				tellerId = (Integer) httpSession.getAttribute("tellerId");
//			}
//
//			/* This method is used to add money to a customer account */
//			accountService.addMoney(accNo, amt);
//
//			Transaction transaction = new Transaction();
//
//			transaction.setAccountNumber(accNo);
//			transaction.setAmount(amt);
//			transaction.setType(TransactionType.CREDIT);
//			transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
//			transaction.setTransactionMode("Teller");
//
//			/*
//			 * This method is used to add the transaction to the database by
//			 * calling the corresponding DAO method
//			 */
//			Transaction transaction2 = transactionService.addTransaction(transaction, "" + tellerId);
//
//			numberOfCustomers = customerService.getNoOfCustomers();
//
//
//			/*
//			 * here we are setting the values to model object which eill be sent
//			 * to client
//			 */
//			model.addObject("addMoneyMessage", environment
//					.getProperty("AddMoneyController.ADD_MONEY_SUCCESS")+transaction2.getTransactionId());
//
//			model.addObject("noOfCustomers", numberOfCustomers);
//
//			logger.info("MONEY ADDED SUCCFULLY TO ACC NO : "+accNo+", AMOUNT : "+amt);
//
//		} catch (Exception exception) {
//			model.addObject("addMoneyErrorMessage",
//					environment.getProperty(exception.getMessage()));
//		}
//
//		return model;
//
//	}

	@RequestMapping(value = "findAccount", method = RequestMethod.POST)
	public ModelAndView findAccountByAccountNumber(@RequestParam String accountNumber)
	{
		ModelAndView modelAndView = new ModelAndView("customerAddMoney");
		try
		{
			Account account = accountService.getAccountByAccountNumber(accountNumber);
			modelAndView.addObject("accountNumber", accountNumber);
			modelAndView.addObject("accountHolderName", account.getAccountHolderName());

			
		}
		catch(Exception e)
		{
			modelAndView.addObject("accountNumber", accountNumber);
			modelAndView.addObject("addMoneyErrorMessage", environment.getProperty(e.getMessage()));
		}
		
		return modelAndView;
		
	}
}
