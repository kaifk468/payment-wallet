package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.amigowallet.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.Merchant;
import com.amigowallet.entity.MerchantTransaction;

import com.amigowallet.entity.User;

import com.amigowallet.model.TransactionStatusDto;
import com.amigowallet.model.UserTransactionDto;
import com.amigowallet.utility.AmigoWalletConstants;

@Repository("billPaymentDAO")
public class BillPaymentDAOImpl implements BillPaymentDAO {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	WalletToWalletDAO walletToWalletDAO;
	
	@Autowired
	UserLoginDAO userLoginDAO;

	@Override
	public List<String> displayServiceType()
	{
		TypedQuery<String> q= entityManager.createQuery("select c.serviceType from MerchantServiceType c",String.class);
		List<String> types=q.getResultList();
		return types;
	}
	
	@Override
	public List<String> displayMerchantName(String type)
	{
		TypedQuery<Integer> q1=entityManager.createQuery("select c.serviceId from MerchantServiceType c where c.serviceType = :st",Integer.class);
		q1.setParameter("st",type);
		Integer sid=q1.getSingleResult();
		TypedQuery<Integer> q2=entityManager.createQuery("select c.merchantId from MerchantServiceMapping c where c.serviceId = :id",Integer.class);
		q2.setParameter("id",sid);
		List<Integer> mid=q2.getResultList();
		TypedQuery<Merchant> q3=entityManager.createQuery("select c from Merchant c ", Merchant.class);
		List<Merchant> mer=q3.getResultList();
		List<String> names=new ArrayList<String>();
		for(Merchant m:mer)
		{
			if(mid.contains(m.getMerchantId())) {
				names.add(m.getName());
			}
		}
		return names;
	}
	
	@Override
	public Integer registerMerchantTransaction(Integer merchantId, Double amount,Integer userId)  throws Exception
	{
	User user=entityManager.find(User.class, userId);

	if(user==null)
	{
		return 0;
	}
	MerchantTransaction mte=new MerchantTransaction();
	mte.setAmount(amount);
	mte.setPaymentTypeId(1);
	mte.setRemarks(AmigoWalletConstants.PAYMENT_TYPE_CREDIT);
	mte.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_CREDIT+user.getEmailId());
	mte.setTransactionStatus(TransactionStatusDto.SUCCESS);
	mte.setMerchantId(merchantId);
	entityManager.persist(mte);
	return 1;
	}
	
	
	@Override
	public Integer registerUserTransaction(Integer userId,Double amountToTransfer,Integer merchantId)  throws Exception
	{
		UserDto sender=userLoginDAO.getUserByUserId(userId);
		if(sender==null)
		{
			return null;
		}
		Merchant mte = entityManager.find(Merchant.class, merchantId);

		Random random=new Random();
		Integer rewardpoint=random.nextInt(5);

		UserTransactionDto userTransactionDtoSender = new UserTransactionDto();
		userTransactionDtoSender.setAmount(amountToTransfer);
		userTransactionDtoSender.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_DEBIT+" "+mte.getEmailId());
		userTransactionDtoSender.setPointsEarned(rewardpoint);
		userTransactionDtoSender.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionDtoSender.setRemarks("D");
		

		if(walletToWalletDAO.walletDebit(userTransactionDtoSender, userId)==0){
			return null;
		}
		return rewardpoint;
	}
	
	@Override
	public Integer findMerchantId(String name)  throws Exception{
		TypedQuery<Integer> q=entityManager.createQuery("select c.merchantId from Merchant c where c.name =:mname",Integer.class);
		q.setParameter("mname", name);
		Integer num=q.getSingleResult();
		return num;
		
	}
	
	

	
}