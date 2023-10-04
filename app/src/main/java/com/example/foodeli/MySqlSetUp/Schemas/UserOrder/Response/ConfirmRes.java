package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class ConfirmRes extends ResponseApi {

    @SerializedName("confirm")
    private boolean confirm;

    public boolean isConfirm() {
        return confirm;
    }
}
