package com.example.foodeli.MySqlSetUp.Schemas.Cart.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class AddToCartRes extends ResponseApi {

    @SerializedName("add")
    private boolean added;

    public boolean isAdded() {
        return added;
    }
}
