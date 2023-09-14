package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.ShopWithRole;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllShopUserHaveResponse extends ResponseApi {

    @SerializedName("shops")
    private ArrayList<ShopWithRole> shops;

    public ArrayList<ShopWithRole> getShops() {
        return shops;
    }
}
