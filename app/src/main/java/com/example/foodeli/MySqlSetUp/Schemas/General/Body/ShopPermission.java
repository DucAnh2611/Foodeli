package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class ShopPermission {
    @SerializedName("PermissionId") private int pid;
    @SerializedName("PermissionType") private String type;
}
