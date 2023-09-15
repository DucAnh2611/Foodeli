package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.ShopPermission;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShopPermissionRes extends ResponseApi {

    @SerializedName("permission") private ArrayList<ShopPermission> permissions;

    public ArrayList<ShopPermission> getPermissions() {
        return permissions;
    }
}
