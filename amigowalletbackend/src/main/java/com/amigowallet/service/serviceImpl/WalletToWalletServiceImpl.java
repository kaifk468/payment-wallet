package com.amigowallet.service.serviceImpl;



import javax.transaction.Transactional;

import com.amigowallet.service.WalletToWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.amigowallet.dao.WalletToWalletDAO;


@Service(value = "walletTransferService")
@Transactional
public class WalletToWalletServiceImpl implements WalletToWalletService {

	@Autowired
	private WalletToWalletDAO walletTransferDAO; 
	
		@Override
		public Integer transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer) throws Exception {
			
			Integer number=walletTransferDAO.transferToWallet(userId, amountToTransfer, emailIdToTransfer);
			if(number==0){
				throw new Exception("WalletService.TRANSACTION_FAILURE");
			}else{
			return number;
			}
		}
	}
	
	
	
	


