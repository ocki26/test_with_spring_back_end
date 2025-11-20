package com.app_language.hoctiengtrung_online.auth.dto;


import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.wallet.model.Wallet;

public class UserProfileResponse {

    private User user;
    private Wallet wallet;

    public UserProfileResponse() {
    }

    public UserProfileResponse(User user, Wallet wallet) {
        this.user = user;
        this.wallet = wallet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
