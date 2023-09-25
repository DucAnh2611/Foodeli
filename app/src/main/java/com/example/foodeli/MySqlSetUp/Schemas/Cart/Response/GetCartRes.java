package com.example.foodeli.MySqlSetUp.Schemas.Cart.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCartRes extends ResponseApi {

    @SerializedName("cart")
    private ArrayList<ProductWithImage> products;

    public class ProductWithImage extends Product {

        @SerializedName("ProductImage")
        private String image;

        @SerializedName("quantity")
        private float quantity;

        public String getImage() {
            return image;
        }

        public float getQuantity() {
            return quantity;
        }
    }

    public ArrayList<ProductWithImage> getProducts() {
        return products;
    }
}
