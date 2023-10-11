package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Product.Product;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Shop;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetProductInfo extends ResponseApi {

    @SerializedName("item")
    private ProductInfo item;

    public static class ProductInfo {

        @SerializedName("data")
        private GetProductWithAvg product;

        @SerializedName("images")
        private ArrayList<ImageWithId> images;

        @SerializedName("shopInfo")
        private Shop shop;

        public static class GetProductWithAvg extends Product {

            @SerializedName("ProductAverageRating")
            private float rate;


            public float getRate() {
                return rate;
            }
        }

        public GetProductWithAvg getProduct() {
            return product;
        }

        public ArrayList<ImageWithId> getImages() {
            return images;
        }

        public Shop getShop() {
            return shop;
        }
    }

    public static class ImageWithId {
        @SerializedName("id")
        private int id;

        @SerializedName("base64")
        private String image;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }
    }

    public ProductInfo getItem() {
        return item;
    }

}
