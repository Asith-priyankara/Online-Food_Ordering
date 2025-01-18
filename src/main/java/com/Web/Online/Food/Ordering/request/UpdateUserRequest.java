package com.Web.Online.Food.Ordering.request;

public class UpdateUserRequest {
    private String fullName;
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public void setFirstName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}