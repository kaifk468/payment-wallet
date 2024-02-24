package com.amigowallet.service.serviceImpl;

import java.util.List;

import com.amigowallet.service.RewardPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.utility.AmigoWalletConstants;

@Service(value = "rewardPointsService")
@Transactional
public class RewardPointsServiceImpl implements RewardPointsService {

	@Autowired
	private RewardPointsDAO rewardPointsDAO;

	@Override
	public void redeemRewardPoints(Integer userId)
			throws Exception {

		List<UserTransactionDto> userTransactionDtos = rewardPointsDAO
				.getAllTransactionByUserId(userId);

		Integer rewardPoints = 0;

		if (userTransactionDtos != null) {
			for (UserTransactionDto userTransactionDto : userTransactionDtos) {
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransactionDto.getIsRedeemed().toString())) {
					rewardPoints += userTransactionDto.getPointsEarned();
				}
			}
		}

		if (rewardPoints < 10) {
			throw new Exception(
					"RewardPointsService.REWARD_POINTS_NOT_ENOUGH_TO_REDEEM");
		}

		/* here we are calculating the amount according to the reward points */
		Double amountToBeCredited = rewardPoints / 10.0;

		/*
		 * here we are redeeming all the reward points by passing userId as
		 * argument to redeemAllRewardPoints method of rewardPointsDAO
		 */
		rewardPointsDAO.redeemAllRewardPoints(userId);

		
		UserTransactionDto userTransactionDto = new UserTransactionDto();
		userTransactionDto.setAmount(amountToBeCredited);
		userTransactionDto
				.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_BY_REDEEMING_REWARD_POINTS);
		userTransactionDto.setPointsEarned(0);
		userTransactionDto
				.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES
						.charAt(0));

		rewardPointsDAO.addRedeemedMoneyToWallet(userId, userTransactionDto);

	}
}
