package com.amigowallet.dao;

import com.amigowallet.model.MoneyRequestDto;
import com.amigowallet.model.UserTransactionDto;

/**
 * This is a DAO interface having method to perform CRUD operations on user and  transactions
 * on performing different transactions like loadMoney, debitMoney etc.
 * 
 * @author ETA_JAVA
 *
 */
public interface UserTransactionDAO {


	UserTransactionDto loadMoneyToWallet(UserTransactionDto userTransactionDto, MoneyRequestDto moneyRequestDto);
	

	UserTransactionDto sendMoneyToBank(UserTransactionDto userTransactionDto, MoneyRequestDto moneyRequestDto);

}
