package com.example.foodeli.MySqlSetUp.Schemas.Address.Response;

import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllAddressRes {

    @SerializedName("address")
    private ArrayList<Address> list;

}
