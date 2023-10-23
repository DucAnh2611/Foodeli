package com.example.foodeli.MySqlSetUp.Schemas.User.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FindUserResponse extends ResponseApi {

    @SerializedName("user")
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }
}
