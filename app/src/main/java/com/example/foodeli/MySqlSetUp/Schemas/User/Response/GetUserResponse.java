package com.example.foodeli.MySqlSetUp.Schemas.User.Response;

import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetUserResponse extends ResponseApi {

    @SerializedName("user")
    private info user;

    public class info {

        @SerializedName("user")
        private User info;

        @SerializedName("address")
        private List<Address> listAddress = new ArrayList<>();

        //more

    }

}
