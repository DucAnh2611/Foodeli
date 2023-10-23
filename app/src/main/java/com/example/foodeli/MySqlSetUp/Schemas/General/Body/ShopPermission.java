package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class ShopPermission {
    @SerializedName("PermissionId") private int pid;
    @SerializedName("PermissionType") private String type;

    public int getPid() {
        return pid;
    }

    public String getType() {
        return type;
    }
}
