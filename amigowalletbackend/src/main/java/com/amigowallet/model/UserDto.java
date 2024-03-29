package com.amigowallet.model;

import java.util.List;

public class UserDto {

	private Integer userId;
	private String emailId;
	private String mobileNumber;
	private String name;
	private String password;
	private UserStatusDto userStatusDto;
	private Double balance;
	private String successMessage;
	private String errorMessage;
	private Integer rewardPoints;
	private List<UserTransactionDto> userTransactionDtos;
	private SecurityQuestionDto securityQuestionDto;
	private String securityAnswer;
	private String newPassword;
	private String confirmNewPassword;
	
	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public Double getBalance() {
		return balance;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getName() {
		return name;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getPassword() {
		return password;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public Integer getUserId() {
		return userId;
	}

	public UserStatusDto getUserStatus() {
		return userStatusDto;
	}

	public List<UserTransactionDto> getUserTransactions() {
		return userTransactionDtos;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserStatus(UserStatusDto userStatusDto) {
		this.userStatusDto = userStatusDto;
	}

	public void setUserTransactions(List<UserTransactionDto> userTransactionDtos) {
		this.userTransactionDtos = userTransactionDtos;
	}

	public SecurityQuestionDto getSecurityQuestion() {
		return securityQuestionDto;
	}

	public void setSecurityQuestion(SecurityQuestionDto securityQuestionDto) {
		this.securityQuestionDto = securityQuestionDto;
	}

	

	
	
}
