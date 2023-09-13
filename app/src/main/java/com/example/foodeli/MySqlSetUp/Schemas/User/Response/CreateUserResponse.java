package com.example.foodeli.MySqlSetUp.Schemas.User.Response;

import com.example.foodeli.MySqlSetUp.Response;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.annotations.SerializedName;

public class CreateUserResponse extends Response {

    @SerializedName("info")
    private User user;
    public User getUser() {return this.user;}
}
