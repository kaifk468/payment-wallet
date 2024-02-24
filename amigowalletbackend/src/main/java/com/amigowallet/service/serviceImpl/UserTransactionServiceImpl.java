package com.amigowallet.service.serviceImpl;

import com.amigowallet.model.MoneyRequestDto;
import com.amigowallet.service.UserTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserTransactionDAO;
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.utility.AmigoWalletConstants;

@Service("userTransactionService")
@Transactional
public class UserTransactionServiceImpl implements UserTransactionService {

	@Autowired
	private UserTransactionDAO userTransactionDAO;
	

	public UserTransactionDto loadMoneyFromBank(MoneyRequestDto moneyRequestDto)
	{

		UserTransactionDto userTransactionDto = new UserTransactionDto();

		userTransactionDto.setAmount(moneyRequestDto.getAmount());
		userTransactionDto.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_DEBIT_CARD);
		userTransactionDto.setPointsEarned(0);
		userTransactionDto.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		userTransactionDto = userTransactionDAO.loadMoneyToWallet(userTransactionDto, moneyRequestDto);

		return userTransactionDto;
	}


	

	@Override
	public UserTransactionDto sendMoneyToBank(MoneyRequestDto moneyRequestDto) {
		UserTransactionDto userTransactionDto = new UserTransactionDto();

		userTransactionDto.setAmount(moneyRequestDto.getAmount());
		userTransactionDto.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_SENT_TO_BANK_ACCOUNT_FROM_EWALLET);
		userTransactionDto.setPointsEarned(0);
		userTransactionDto.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));

		userTransactionDto = userTransactionDAO.sendMoneyToBank(userTransactionDto, moneyRequestDto);

		return userTransactionDto;
	}
}
