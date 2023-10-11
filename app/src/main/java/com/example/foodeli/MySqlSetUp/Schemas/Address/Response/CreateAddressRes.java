package com.example.foodeli.MySqlSetUp.Schemas.Address.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.google.gson.annotations.SerializedName;

public class CreateAddressRes extends ResponseApi {

    @SerializedName("address")
    private Address address;

    public Address getAddress() {
        return address;
    }
}
