package com.wiser.kids.model.response;


import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.User;

/**
 * Created by sidhu on 4/11/2018.
 */

public class GetAccountResponse extends BaseResponse {

    @SerializedName("user")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
