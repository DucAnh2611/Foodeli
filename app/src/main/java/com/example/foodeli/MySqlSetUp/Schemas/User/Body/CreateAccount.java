package com.example.foodeli.MySqlSetUp.Schemas.User.Body;

public class CreateAccount {
    private String fullname, email, password, phone, birthday;

    CreateAccount(String fullname, String email, String password, String phone, String birthday) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.birthday = birthday;
    }
}
