package com.uiu.kids.event;

import com.uiu.kids.model.User;

public class LoginEvent {


    private User account;
    public LoginEvent(User account) {
        this.account = account;
    }
    public User getAccount() {
        return account;
    }


}
