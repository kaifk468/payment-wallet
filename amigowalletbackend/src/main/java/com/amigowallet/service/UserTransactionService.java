package com.amigowallet.service;

import com.amigowallet.model.MoneyRequestDto;
import com.amigowallet.model.UserTransactionDto;

public interface UserTransactionService {


	UserTransactionDto loadMoneyFromBank(MoneyRequestDto moneyRequestDto);


	UserTransactionDto sendMoneyToBank(MoneyRequestDto moneyRequestDto);
}
