package com.amigowallet.dao;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import com.amigowallet.model.PaymentTypeDto;
import com.amigowallet.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.PaymentType;
import com.amigowallet.entity.User;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.TransactionStatusDto;
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.utility.AmigoWalletConstants;

@Repository("WalletToWalletDAO")
public class WalletToWalletDAOImpl implements WalletToWalletDAO{
	
	@Autowired
	UserLoginDAO userLoginDAO;

	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Integer transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer) throws Exception{
		
		// TODO Auto-generated method stub
		
		UserDto receiver=userLoginDAO.getUserByEmailId(emailIdToTransfer);
		UserDto sender=userLoginDAO.getUserByUserId(userId);
		Double number=0.0;
		if(sender==receiver) {
			throw new Exception("WalletService.PAYING_HIMSELF");
		}
		
		if(sender==null){
			return 0;
		}
		
		if(receiver==null){
			throw new Exception("WalletToWalletService.INVALID_EMAIL");
		}
		
		if(amountToTransfer>=200){
			number=amountToTransfer*0.02;
		}
		
		Random random=new Random();

		Integer rewardpoint=random.nextInt(5);

		UserTransactionDto userTransactionDtoSender = new UserTransactionDto();
		
		/*
		 * A new userTransaction is created here and all the properties are populated
		 */
		userTransactionDtoSender.setAmount(amountToTransfer);
		userTransactionDtoSender.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_DEBIT+" "+emailIdToTransfer);
		userTransactionDtoSender.setPointsEarned(rewardpoint);
		userTransactionDtoSender.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionDtoSender.setRemarks("D");
		
		
		UserTransactionDto userTransactionDtoReceiver = new UserTransactionDto();
		userTransactionDtoReceiver.setAmount(amountToTransfer);
		userTransactionDtoReceiver.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_CREDIT+" "+sender.getEmailId());
		userTransactionDtoReceiver.setPointsEarned(0);
		userTransactionDtoReceiver.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionDtoReceiver.setRemarks("C");
		
		if(walletDebit(userTransactionDtoSender, userId)==0 || walletCredit(userTransactionDtoReceiver, receiver.getUserId())==0){
			return 0;
		}
		
		if(number!=0.0){
		UserTransactionDto userTransactionDtoSender1 = new UserTransactionDto();
		
		/*
		 * A new userTransaction is created here for cashback and all the properties are populated
		 */
		userTransactionDtoSender1.setAmount(number);
		userTransactionDtoSender1.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_CASHBACK_TO_WALLET_CREDIT+" "+number);
		userTransactionDtoSender1.setPointsEarned(0);
		userTransactionDtoSender1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionDtoSender1.setRemarks("C");
		
		walletCredit(userTransactionDtoSender1, userId);
		}
		return 1;
	}
	/**
	 * This method is used to Debit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransactionDto,userID
	 * 
	 * @return Integer 
	 */
	public Integer walletDebit(UserTransactionDto userTransactionDto, Integer userId) {
		
		User user = entityManager.find(User.class, userId);
		
		if(user ==null){
			return 0;
		}
		List<UserTransactionEntity> transactionEntities = user.getUserTransactionEntities();
		
		UserTransactionEntity transactionEntity = new UserTransactionEntity();
		transactionEntity.setAmount(userTransactionDto.getAmount());
		transactionEntity.setIsRedeemed(userTransactionDto.getIsRedeemed());
		transactionEntity.setInfo(userTransactionDto.getInfo());
	
		
		/*
		 * The following code is to set the paymentTypeEntity to transactionEntity
		 */

		Query query = entityManager.createQuery(
				"from PaymentType where paymentFrom = :paymentFrom and paymentTo = :paymentTo and paymentType = :paymentType");
		query.setParameter("paymentFrom", AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0));
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0));
		query.setParameter("paymentType", AmigoWalletConstants.PAYMENT_TYPE_DEBIT.charAt(0));
		PaymentType paymentTypeEntity = (PaymentType) query.getSingleResult();
		
		transactionEntity.setPaymentTypeEntity(paymentTypeEntity);
		transactionEntity.setPointsEarned(userTransactionDto.getPointsEarned());
		transactionEntity.setRemarks(userTransactionDto.getRemarks());
		
		/*
		 * The following code is to set the StatusEntity to transactionEntity
		 */

		transactionEntity.setTransactionStatus(TransactionStatusDto.SUCCESS);

		transactionEntities.add(transactionEntity);
		entityManager.persist(transactionEntity);
		user.setUserTransactionEntities(transactionEntities);
		
		/*
		 * The userEntity is saved to the database
		 */
		entityManager.persist(user);
		userTransactionDto.setUserTransactionId(transactionEntity.getUserTransactionId());
		
		/*
		 * The following code is to set the paymenType model object to transaction model object
		 */
		if (paymentTypeEntity != null) {
			
			PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
			paymentTypeDto.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
			paymentTypeDto.setPaymentTo(paymentTypeEntity.getPaymentTo());
			paymentTypeDto.setPaymentType(paymentTypeEntity.getPaymentType());
			paymentTypeDto.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

			userTransactionDto.setPaymentType(paymentTypeDto);
		}
		
		userTransactionDto.setTransactionStatus(transactionEntity.getTransactionStatus());
		return 1;
	}
	/**
	 * This method is used to Credit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransactionDto,userID
	 * 
	 * @return Integer 
	 */
public Integer walletCredit(UserTransactionDto userTransactionDto, Integer userId) {
		
		User user = entityManager.find(User.class, userId);
		if(user ==null){
			return 0;
		}
		List<UserTransactionEntity> transactionEntities = user.getUserTransactionEntities();
		
		UserTransactionEntity transactionEntity = new UserTransactionEntity();
		transactionEntity.setAmount(userTransactionDto.getAmount());
		transactionEntity.setIsRedeemed(userTransactionDto.getIsRedeemed());
		transactionEntity.setInfo(userTransactionDto.getInfo());
		
		/*
		 * The following code is to set the paymentTypeEntity to transactionEntity
		 */

		Query query = entityManager.createQuery(
				"from PaymentType where paymentFrom = :paymentFrom and paymentTo = :paymentTo and paymentType = :paymentType");
		query.setParameter("paymentFrom", AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0));
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0));
		query.setParameter("paymentType", AmigoWalletConstants.PAYMENT_TYPE_CREDIT.charAt(0));
		PaymentType paymentTypeEntity = (PaymentType) query.getSingleResult();
		
		transactionEntity.setPaymentTypeEntity(paymentTypeEntity);
		transactionEntity.setPointsEarned(userTransactionDto.getPointsEarned());
		transactionEntity.setRemarks(userTransactionDto.getRemarks());
		
		/*
		 * The following code is to set the StatusEntity to transactionEntity
		 */

		transactionEntity.setTransactionStatus(TransactionStatusDto.SUCCESS);

		transactionEntities.add(transactionEntity);
		entityManager.persist(transactionEntity);
		user.setUserTransactionEntities(transactionEntities);
		
		/*
		 * The userEntity is saved to the database
		 */
		entityManager.persist(user);
		userTransactionDto.setUserTransactionId(transactionEntity.getUserTransactionId());
		
		/*
		 * The following code is to set the paymenType model object to transaction model object
		 */
		if (paymentTypeEntity != null) {
			
			PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
			paymentTypeDto.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
			paymentTypeDto.setPaymentTo(paymentTypeEntity.getPaymentTo());
			paymentTypeDto.setPaymentType(paymentTypeEntity.getPaymentType());
			paymentTypeDto.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

			userTransactionDto.setPaymentType(paymentTypeDto);
		}
		
		userTransactionDto.setTransactionStatus(transactionEntity.getTransactionStatus());
		return 1;
	}

	

}
