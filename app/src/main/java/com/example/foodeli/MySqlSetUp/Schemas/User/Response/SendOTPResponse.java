package com.example.foodeli.MySqlSetUp.Schemas.User.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class SendOTPResponse extends ResponseApi {

    @SerializedName("sent")
    private boolean sent;

    @SerializedName("UserID")
    private int uid;

    public boolean isSent() {
        return sent;
    }

    public int getUid() {
        return uid;
    }
}
