package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class MangeUserRes extends ResponseApi {

    @SerializedName("action")
    private boolean action;

    public boolean getAction() {
        return this.action;
    }
}
