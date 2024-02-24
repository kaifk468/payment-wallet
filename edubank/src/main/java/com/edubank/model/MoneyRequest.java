package com.edubank.model;

import lombok.Data;

@Data
public class MoneyRequest {

    private String accountNumber;
    private String ifscCode;
    private Double amount;
    private String password;
    private String transactionType;
    private String userId;
}
