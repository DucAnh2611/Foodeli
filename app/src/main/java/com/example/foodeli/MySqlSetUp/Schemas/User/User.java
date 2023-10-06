package com.example.foodeli.MySqlSetUp.Schemas.User;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("UserID")
    private int id;
    @SerializedName("UserFullName")
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegisAt() {
        return regisAt;
    }

    public String getModified() {
        return modified;
    }
    public String getEmail() {
        return this.email;
    }
    public String getMobile() {
        return this.phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setRegisAt(String regisAt) {
        this.regisAt = regisAt;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
