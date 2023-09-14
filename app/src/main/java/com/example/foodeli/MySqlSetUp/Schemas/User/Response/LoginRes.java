package com.example.foodeli.MySqlSetUp.Schemas.User.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.annotations.SerializedName;

public class LoginRes extends ResponseApi {

    @SerializedName("user")
    private User user;

    public User getUser() {return this.user;}
}
