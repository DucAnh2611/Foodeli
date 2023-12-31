package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetShopInformationResponse extends ResponseApi {

    @SerializedName("shop")
    private GetAllShopUserHaveResponse.ShopWithDetail info;

    public GetAllShopUserHaveResponse.ShopWithDetail getInfo() {
        return info;
    }
}
