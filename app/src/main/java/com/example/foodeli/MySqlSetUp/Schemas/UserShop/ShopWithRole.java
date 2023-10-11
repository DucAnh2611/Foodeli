package com.example.foodeli.MySqlSetUp.Schemas.UserShop;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShopWithRole extends Shop implements Serializable {

    @SerializedName("Role")
    private String role;

    public String getRole() {
        return role;
    }
}
