package com.Web.Online.Food.Ordering.response;

import com.Web.Online.Food.Ordering.model.USER_ROLE;

public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }

    public USER_ROLE getRole() {
        return role;
    }
}
