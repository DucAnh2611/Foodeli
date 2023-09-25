package com.example.foodeli.MySqlSetUp.Schemas.Cart.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class UpdateItemRes extends ResponseApi {

    @SerializedName("update")
    private boolean update;

    public boolean isUpdate() {
        return update;
    }
}