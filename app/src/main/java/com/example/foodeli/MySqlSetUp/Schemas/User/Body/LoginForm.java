package com.example.foodeli.MySqlSetUp.Schemas.User.Body;

public class LoginForm {
    private String email, phone, password;

    public LoginForm(String email, String phone, String password){
        this.email = email;
        this.phone= phone;
        this.password = password;
    }

    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setPassword(String password) {this.password = password;}

}
