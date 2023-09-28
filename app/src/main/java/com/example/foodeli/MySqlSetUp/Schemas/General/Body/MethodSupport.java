package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class MethodSupport {
    @SerializedName("MethodId") private int mid;
    @SerializedName("MethodType") private String type;

    public int getMid() {
        return mid;
    }

    public String getType() {
        return type;
    }
}
