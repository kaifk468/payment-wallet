package com.amigowallet.service.serviceImpl;

import java.util.List;

import com.amigowallet.model.UserDto;
import com.amigowallet.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.UserStatusDto;
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.HashingUtility;
import com.amigowallet.validator.UserLoginValidator;

@Service(value = "userLoginService")
@Transactional
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private UserLoginDAO userLoginDAO;

	@Override
	public UserDto authenticate(UserDto userDto) throws Exception {

		UserLoginValidator.validateUserLogin(userDto);

		UserDto userDtoFromDao = userLoginDAO.getUserByEmailId(userDto.getEmailId());

		if (userDtoFromDao == null) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}

		String hashedPassword = HashingUtility.getHashValue(userDto.getPassword());
		if (!hashedPassword.equals(userDtoFromDao.getPassword())) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}

		if(!userDtoFromDao.getUserStatus().equals(UserStatusDto.ACTIVE))
		{
			throw new Exception("UserLoginService.INACTIVE_USER");
		}
		
		userDtoFromDao.setPassword(null);
		List<UserTransactionDto> transactions = userDtoFromDao.getUserTransactions();

		Double balance = 0.0;
		Integer nonRedeemedPoints = 0;

		if (transactions != null) {
			for (UserTransactionDto userTransactionDto : transactions) {

				if (AmigoWalletConstants.PAYMENT_TYPE_DEBIT
						.equals(userTransactionDto.getPaymentType()
								.getPaymentType().toString())) {
					balance -= userTransactionDto.getAmount();
				} else {
					balance += userTransactionDto.getAmount();
				}
				
				/* here reward points is calculated which is yet t be redeemed */
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransactionDto.getIsRedeemed().toString())) {
					nonRedeemedPoints += userTransactionDto.getPointsEarned();
				}
			}
		}

		userDtoFromDao.setBalance(balance);
		userDtoFromDao.setRewardPoints(nonRedeemedPoints);
		
		return userDtoFromDao;

	}

	@Override
	public void changeUserPassword(UserDto userDto)
			throws Exception {

		UserLoginValidator.validateChangePasswordDetails(userDto);

		UserDto userDtoFromDao = userLoginDAO.getUserByUserId(userDto.getUserId());

		if (userDtoFromDao == null)
			throw new Exception("LoginService.USER_NOT_FOUND");

		if (!HashingUtility.getHashValue(userDto.getPassword()).equals(
				userDtoFromDao.getPassword())) {
			throw new Exception("LoginService.INVALID_PASSWORD");
		}
		userDto.setNewPassword(HashingUtility.getHashValue(userDto.getNewPassword()));

		userLoginDAO.changeUserPassword(userDto);
	
	}

	@Override
	public UserDto getUserbyUserId(Integer userId)
			throws Exception {
		UserDto userDtoFromDao = userLoginDAO.getUserByUserId(userId);

		if (userDtoFromDao == null) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}

		userDtoFromDao.setPassword(null);

		List<UserTransactionDto> transactions = userDtoFromDao.getUserTransactions();
		Double balance = 0.0;
		Integer nonRedeemedPoints = 0;

		if (transactions != null) {
			for (UserTransactionDto userTransactionDto : transactions) {
				if (AmigoWalletConstants.PAYMENT_TYPE_DEBIT
						.equals(userTransactionDto.getPaymentType()
								.getPaymentType().toString())) {
					balance -= userTransactionDto.getAmount();
				} else {
					balance += userTransactionDto.getAmount();
				}
				
				/* here reward points is calculated which is yet t be redeemed */
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransactionDto.getIsRedeemed().toString())) {
					nonRedeemedPoints += userTransactionDto.getPointsEarned();
				}
			}
		}
		userDtoFromDao.setBalance(balance);
		userDtoFromDao.setRewardPoints(nonRedeemedPoints);
		return userDtoFromDao;
	}
}
