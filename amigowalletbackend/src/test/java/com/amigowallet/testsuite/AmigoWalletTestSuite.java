package com.amigowallet.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.amigowallet.dao.test.BillPaymentDAOTest;
import com.amigowallet.dao.test.DebitCardDAOTest;
import com.amigowallet.dao.test.ForgotPasswordDAOTest;
import com.amigowallet.dao.test.RegistrationDAOTest;
import com.amigowallet.dao.test.RewardPointsDAOTest;
import com.amigowallet.dao.test.UserDtoLoginDAOTest;
import com.amigowallet.dao.test.UserDtoTransactionDAOTest;
import com.amigowallet.dao.test.WalletToWalletDAOTest;
import com.amigowallet.service.test.BillPaymentServiceTest;
import com.amigowallet.service.test.DebitCardServiceTest;
import com.amigowallet.service.test.ForgotPasswordServiceTest;
import com.amigowallet.service.test.RegistrationServiceTest;
import com.amigowallet.service.test.RewardPointsServiceTest;
import com.amigowallet.service.test.TransactionHistoryServiceTest;
import com.amigowallet.service.test.UserDtoLoginServiceTest;
import com.amigowallet.service.test.UserDtoTransactionServiceTest;

import com.amigowallet.service.test.WalletToWalletServiceTest;
import com.amigowallet.validator.test.RegistrationValidatorTest;
import com.amigowallet.validator.test.UserDtoLoginValidatorTest;

/**
 * this will run all the test cases at a time to include the test
 * cases write all the test class in SuiteClasses

 * @author ETA_JAVA
 *
 */
@RunWith(Suite.class)
/** here we are including all the test cases files which needs to be tested */
@Suite.SuiteClasses({ 
	
	DebitCardDAOTest.class,
	ForgotPasswordDAOTest.class,
	RegistrationDAOTest.class,
	RewardPointsDAOTest.class,
	UserDtoLoginDAOTest.class,
	UserDtoTransactionDAOTest.class,
	WalletToWalletDAOTest.class,
	BillPaymentDAOTest.class,
	
	DebitCardServiceTest.class,
	ForgotPasswordServiceTest.class,
	RegistrationServiceTest.class,
	RewardPointsServiceTest.class,
	UserDtoLoginServiceTest.class,
	UserDtoTransactionServiceTest.class,
	WalletToWalletServiceTest.class,
	TransactionHistoryServiceTest.class,
	BillPaymentServiceTest.class,
	
	UserDtoLoginValidatorTest.class,
	RegistrationValidatorTest.class
})
public class AmigoWalletTestSuite {

}
