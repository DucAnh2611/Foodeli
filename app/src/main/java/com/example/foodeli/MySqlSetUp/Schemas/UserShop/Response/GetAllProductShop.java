package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllProductShop {
    @SerializedName("shop")
    private ArrayList<GetTopProduct.ProductWithAvg> products;

    public ArrayList<GetTopProduct.ProductWithAvg> getProducts() {
        return products;
    }
}
