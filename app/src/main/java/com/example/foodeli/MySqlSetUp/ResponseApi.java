package com.example.foodeli.MySqlSetUp;

import com.google.gson.annotations.SerializedName;

public class ResponseApi {

    @SerializedName("success")
    private boolean success = false;

    @SerializedName("error")
    private String message;

    public boolean getStatus() {return this.success;}

    public String getMessage() {return this.message;}

}
