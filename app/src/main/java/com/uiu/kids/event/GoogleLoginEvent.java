package com.uiu.kids.event;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleLoginEvent {


    private GoogleSignInAccount account;
    public GoogleLoginEvent(GoogleSignInAccount account) {
        this.account = account;
    }
    public GoogleSignInAccount getAccount() {
        return account;
    }


}
