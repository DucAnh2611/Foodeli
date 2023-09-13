package com.example.foodeli.MySqlSetUp.Schemas.User;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User {

    @SerializedName("UserID")
    private int id;
    @SerializedName("UserFullname")
    private String name;
    @SerializedName("UserPassword")
    private String password;
    @SerializedName("UserAvatar")
    private String avatar;
    @SerializedName("UserPhone")
    private String phone;
    @SerializedName("UserEmail")
    private String email;
    @SerializedName("UserBirthday")
    private String birthday;
    @SerializedName("UserRegistrationDate")
    private String regisAt;
    @SerializedName("UserLastModifiedDate")
    private String modified;

    public User(
            int id,
            String name,
            String phone,
            String email,
            String birthday,
            String regisAt,
            String modified
    ) {
        this.name = name;
        this.phone = phone;
        this.email = email;

        this.birthday = birthday;
        this.regisAt = regisAt;
        this.modified = modified;
    };

    public User(
            String phone,
            String email,
            String password
    ) {
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public void setAvatar(String avatar) { this.avatar = avatar; };

    public String getEmail() {
        return this.email;
    }

    public String getMobile() {
        return this.phone;
    }
}
