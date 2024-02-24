package com.amigowallet.service;

import java.util.List;

import com.amigowallet.model.UserTransactionDto;


public interface TransactionHistoryService {

	public List<UserTransactionDto> getAllTransactionByUserId(Integer userId) throws Exception;
}
