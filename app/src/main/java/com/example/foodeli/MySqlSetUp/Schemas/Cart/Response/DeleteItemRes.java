package com.example.foodeli.MySqlSetUp.Schemas.Cart.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class DeleteItemRes extends ResponseApi {

    @SerializedName("delete")
    private boolean delete;

    public boolean isDelete() {
        return delete;
    }
}