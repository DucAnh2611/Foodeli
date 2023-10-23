package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Shop;
import com.google.gson.annotations.SerializedName;

public class CreateShopResponse extends ResponseApi {

    @SerializedName("shop")
    private GetAllShopUserHaveResponse.ShopWithDetail shop;

    public GetAllShopUserHaveResponse.ShopWithDetail getShop() {
        return shop;
    }
}
