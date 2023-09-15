package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class CancelRes extends ResponseApi {

    @SerializedName("cancel")
    private boolean cancel;

    public boolean getCancel() {
        return this.cancel;
    }
}
