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
        private int quantity;

        public String getImage() {
            return image;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public ArrayList<ProductWithImage> getProducts() {
        return products;
    }
}
