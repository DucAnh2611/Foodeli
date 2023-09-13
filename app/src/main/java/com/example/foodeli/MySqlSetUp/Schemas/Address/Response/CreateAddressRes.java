package com.example.foodeli.MySqlSetUp.Schemas.Address.Response;

import com.example.foodeli.MySqlSetUp.Response;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.google.gson.annotations.SerializedName;

public class CreateAddressRes extends Response {

    @SerializedName("address")
    private Address address;

}
