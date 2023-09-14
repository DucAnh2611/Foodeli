package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FindProductRes extends ResponseApi {

    @SerializedName("item")
    private ArrayList<Item> item;

    public class Item {
        @SerializedName("data")
        private Product data;

        @SerializedName("image")
        private String image;
    }

}
