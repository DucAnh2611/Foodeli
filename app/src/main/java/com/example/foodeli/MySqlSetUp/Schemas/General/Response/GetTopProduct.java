package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetTopProduct extends ResponseApi {

    @SerializedName("items")
    private ArrayList<ProductWithAvg> list;

    public static class ProductWithAvg extends Product {


        @SerializedName("ProductAverageRating")
        private float avg;

        @SerializedName("ProductImage")
        private String pimage;

        @SerializedName("ShopImage")
        private String simage;

        public float getAvg() {
            return avg;
        }

        public String getPImage() {
            return pimage;
        }

        public String getSImage() {
            return simage;
        }
    }

    public ArrayList<ProductWithAvg> getList() {
        return list;
    }
}
