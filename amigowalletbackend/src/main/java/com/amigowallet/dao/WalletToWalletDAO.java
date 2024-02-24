package com.amigowallet.dao;


import com.amigowallet.model.UserTransactionDto;

public interface WalletToWalletDAO {
	 
	/**
	 * This method is used to transfer money from sender's ID to receiver's emailId with amount passed as parameters
	 * 
	 * @param userID,amountToTransfer,emailIdToTransfer
	 * 
	 * @return Integer 
	 */
	public Integer transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer) throws Exception;
	/**
	 * This method is used to Debit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransactionDto,userID
	 * 
	 * @return Integer 
	 */
	public Integer walletDebit(UserTransactionDto userTransactionDto, Integer userId) throws Exception;
	/**
	 * This method is used to Credit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransactionDto,userID
	 * 
	 * @return Integer 
	 */
	public Integer walletCredit(UserTransactionDto userTransactionDto, Integer userId) throws Exception;
}


