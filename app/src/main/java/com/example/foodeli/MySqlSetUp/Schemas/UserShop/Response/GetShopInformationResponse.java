package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetShopInformationResponse extends ResponseApi {

    @SerializedName("shop")
    private ShopWithProducts shop;

    public ShopWithProducts getShop() {
        return shop;
    }

    public class ShopWithProducts {
        @SerializedName("info")
        private GetAllShopUserHaveResponse.ShopWithDetail info;

        @SerializedName("products")
        private ArrayList<GetTopProduct.ProductWithAvg> products;

        public ArrayList<GetTopProduct.ProductWithAvg> getProducts() {
            return products;
        }

        public GetAllShopUserHaveResponse.ShopWithDetail getInfo() {
            return info;
        }
    }
}
