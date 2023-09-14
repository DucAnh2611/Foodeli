package com.example.foodeli.MySqlSetUp.Schemas.UserShop;

import com.google.gson.annotations.SerializedName;

public class ShopWithRole extends Shop{

    @SerializedName("Role")
    private String role;


}
