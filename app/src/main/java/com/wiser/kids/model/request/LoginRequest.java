package com.wiser.kids.model.request;

public class LoginRequest {

    public User user;

    public void setUser(User user) {
        this.user = user;
    }

    public class User{
        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        String email;
        String password;
    }
}
