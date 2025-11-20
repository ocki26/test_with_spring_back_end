package com.app_language.hoctiengtrung_online.wallet.dto;

import java.math.BigDecimal;

public class DepositRequest {
    private String userId;
    private BigDecimal amount;
    private String method; // MoMo, ZaloPay, v.v.

    public DepositRequest(String userId, BigDecimal amount, String method) {
        this.userId = userId;
        this.amount = amount;
        this.method = method;
    }

    public DepositRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
