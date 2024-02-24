package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.amigowallet.model.PaymentTypeDto;
import com.amigowallet.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.PaymentType;
import com.amigowallet.entity.User;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.UserTransactionDto;

/**
 * This is a DAO class that contains the methods responsible for interacting the database
 * with respect to Login module like  getUserByEmailId, change user password, getUserByUserId.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "userLoginDAO")
public class UserLoginDAOImpl implements UserLoginDAO {
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;

	/**
	 * This method is used to get a User model corresponding to its emailId<br>
	 * The transaction and card details are also fetched along with the User.
	 * 
	 * @param emailId
	 * 
	 * @return User
	 */
	@Override
	@SuppressWarnings("unchecked")
	public UserDto getUserByEmailId(String emailId) throws Exception {
		
		emailId = emailId.toLowerCase();
		UserDto userDtoFromDb = null;
		
		/*
		 * The userEntity with the required emailId is fetched 
		 */
		Query query = entityManager.createQuery("from User where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		
		List<User> userEntities = query.getResultList();
		
		/*
		 * A user bean is populated from the data obtained from the 
		 * above fetched userEntity
		 */
		if (!userEntities.isEmpty()) {
			User user = userEntities.get(0);
			userDtoFromDb = new UserDto();
			userDtoFromDb.setEmailId(user.getEmailId());
			userDtoFromDb.setMobileNumber(user.getMobileNumber());
			userDtoFromDb.setName(user.getName());
			userDtoFromDb.setPassword(user.getPassword());
			userDtoFromDb.setUserId(user.getUserId());
			userDtoFromDb.setUserStatus(user.getUserStatus());
			
			/*
			 * The below lines of code fetches the card details of the user.
			 */
//			List<CardEntity> cardEntities = userEntity.getCardEntities();
//
//			List<Card> cards = null;
//			if (cardEntities != null && !cardEntities.isEmpty()) {
//				cards = new ArrayList<>();
//
//				/*
//				 * For each cardEntity in the list of cardEntities
//				 * a Bank bean is populated with the corresponding properties
//				 * of the bankEntity
//				 */
//				for (CardEntity cardEntity : cardEntities) {
//
//					Card card =new Card();
//					BankEntity bankEntity = cardEntity.getBankEntity();
//					Bank bank = new Bank();
//					bank.setBankId(bankEntity.getBankId());
//					bank.setBankName(bankEntity.getBankName());
//					card.setBank(bank);
//
//					card.setCardId(cardEntity.getCardId());
//					card.setCardNumber(cardEntity.getCardNumber());
//					card.setExpiryDate(cardEntity.getExpiryDate());
//					card.setCardStatus(cardEntity.getUserStatus());
//					cards.add(card);
//				}
//				userFromDb.setCards(cards);
//			}

			/*
			 * The below lines of code fetches all the points
			 * earned and transaction details of the user.
			 */
			List<UserTransactionEntity> userTransactionEntities = user.getUserTransactionEntities();
			
			List<UserTransactionDto> transactions = null;
			if (userTransactionEntities != null && !userTransactionEntities.isEmpty()) {
				transactions = new ArrayList<UserTransactionDto>();
				for (UserTransactionEntity userTransactionEntity : userTransactionEntities) {
					
					UserTransactionDto transaction = new UserTransactionDto();
					transaction.setAmount(userTransactionEntity.getAmount());
					transaction.setInfo(userTransactionEntity.getInfo());
					transaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());
					
					/*
					 * The following code is to set the payment Type model object to transaction model object
					 */
					PaymentType paymentTypeEntity = userTransactionEntity.getPaymentTypeEntity();
					if (paymentTypeEntity != null) {
						
						PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
						paymentTypeDto.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
						paymentTypeDto.setPaymentTo(paymentTypeEntity.getPaymentTo());
						paymentTypeDto.setPaymentType(paymentTypeEntity.getPaymentType());
						paymentTypeDto.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

						transaction.setPaymentType(paymentTypeDto);
					}
					transaction.setPointsEarned(userTransactionEntity.getPointsEarned());
					transaction.setRemarks(userTransactionEntity.getRemarks());

					transaction.setTransactionStatus(userTransactionEntity.getTransactionStatus());
					transaction.setTransactionDateTime(userTransactionEntity.getTransactionDateTime());
					transaction.setUserTransactionId(userTransactionEntity.getUserTransactionId());
					transactions.add(transaction);
				}
				userDtoFromDb.setUserTransactions(transactions);
			}
		}
		return userDtoFromDb;
	}
	
	/**
	 * This method is used to get a User model corresponding to its userId<br>
	 * The transaction and card details are also fetched along with the User.
	 * 
	 * @param emailId
	 * 
	 * @return User
	 */
	@Override
	public UserDto getUserByUserId(Integer userId) {
		UserDto userDtoFromDb = null;
		
		User user = entityManager.find(User.class, userId);

		if (user != null) {
			
			userDtoFromDb = new UserDto();
			userDtoFromDb.setEmailId(user.getEmailId());
			userDtoFromDb.setMobileNumber(user.getMobileNumber());
			userDtoFromDb.setName(user.getName());
			userDtoFromDb.setPassword(user.getPassword());
			userDtoFromDb.setUserId(user.getUserId());
			userDtoFromDb.setUserStatus(user.getUserStatus());
			
			/*
//			 * The below lines of code fetches the card details of the user.
//			 */
//			List<CardEntity> cardEntities = userEntity.getCardEntities();
//
//			List<Card> cards = null;
//			if (cardEntities != null && !cardEntities.isEmpty()) {
//				cards = new ArrayList<>();
//
//				/*
//				 * For each cardEntity in the list of cardEntities
//				 * a Bank bean is populated with the corresponding properties
//				 * of the bankEntity
//				 */
//				for (CardEntity cardEntity : cardEntities) {
//					Card card = new Card();
//
//					BankEntity bankEntity = cardEntity.getBankEntity();
//					Bank bank = new Bank();
//					bank.setBankId(bankEntity.getBankId());
//					bank.setBankName(bankEntity.getBankName());
//					card.setBank(bank);
//
//					card.setCardId(cardEntity.getCardId());
//					card.setCardNumber(cardEntity.getCardNumber());
//					card.setExpiryDate(cardEntity.getExpiryDate());
//					card.setCardStatus(cardEntity.getUserStatus());
//					cards.add(card);
//				}
//				userFromDb.setCards(cards);
//			}

			/*
			 * The below lines of code fetches all the transaction and points earned details of the user.
			 */
			List<UserTransactionEntity> userTransactionEntities = user.getUserTransactionEntities();
			
			List<UserTransactionDto> transactions = null;
			if (userTransactionEntities != null&& !userTransactionEntities.isEmpty()) {
				transactions = new ArrayList<UserTransactionDto>();
				for (UserTransactionEntity userTransactionEntity : userTransactionEntities) {
					
					UserTransactionDto transaction = new UserTransactionDto();
					transaction.setAmount(userTransactionEntity.getAmount());
					transaction.setInfo(userTransactionEntity.getInfo());
					transaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());
					
					/*
					 * The following code is to set the payment Type model object to transaction model object
					 */
					PaymentType paymentTypeEntity = userTransactionEntity.getPaymentTypeEntity();
					if (paymentTypeEntity != null) {
						
						PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
						paymentTypeDto.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
						paymentTypeDto.setPaymentTo(paymentTypeEntity.getPaymentTo());
						paymentTypeDto.setPaymentType(paymentTypeEntity.getPaymentType());
						paymentTypeDto.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());
						transaction.setPaymentType(paymentTypeDto);
					}
					transaction.setPointsEarned(userTransactionEntity.getPointsEarned());
					transaction.setRemarks(userTransactionEntity.getRemarks());
					
					transaction.setTransactionStatus(userTransactionEntity.getTransactionStatus());
					transaction.setTransactionDateTime(userTransactionEntity.getTransactionDateTime());
					transaction.setUserTransactionId(userTransactionEntity.getUserTransactionId());
					transactions.add(transaction);
				}
				userDtoFromDb.setUserTransactions(transactions);
			}
		}
		return userDtoFromDb;
	}
	
	/**
	 * This method is used to change the password of an existing User. It
	 * takes UserLogin as a parameter, which includes, userId and
	 * newPassword, it fetches an entity on the basis of userId, and updates
	 * the password to newPassword received.<br>
	 * 
	 * @param userDto
	 */
	@Override
	public void changeUserPassword(UserDto userDto) {
		
		User userEntity = entityManager.find(User.class, userDto.getUserId());
		userEntity.setPassword(userDto.getNewPassword());
	}
}
