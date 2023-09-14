package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateProductRes extends ResponseApi {

    @SerializedName("p")
    private P product;

    public class P {

        @SerializedName("data")
        private Product product;

        @SerializedName("category")
        private ArrayList<Category> categories;

        @SerializedName("images")
        private ArrayList<Image> images;
    }

    public class Category {
        @SerializedName("CategoryID")
        private int id;

        @SerializedName("CategoryName")
        private String name;
    }

    public class Image {
        @SerializedName("id")
        private int iid;

        @SerializedName("base64")
        private String base64;
    }


}
