package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Shop;
import com.google.gson.annotations.SerializedName;

public class GetShopInfomationResponse extends ResponseApi {

    @SerializedName("shops")
    private Shop shop;

    public Shop getShop() {
        return shop;
    }
}
