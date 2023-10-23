package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.ShopWithRole;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllShopUserHaveResponse extends ResponseApi implements Serializable {

    @SerializedName("shop")
    private ArrayList<ShopWithDetail> shops;

    public ArrayList<ShopWithDetail> getShops() {
        return shops;
    }


    public static class ShopWithDetail extends ShopWithRole implements Serializable {
        @SerializedName("ProductQuantity")
        private int productQuantity;

        @SerializedName("ShopRating")
        private float shopRating;

        @SerializedName("Sold")
        private int sold;

        public int getProductQuantity() {
            return productQuantity;
        }

        public float getShopRating() {
            return shopRating;
        }

        public int getSold() {
            return sold;
        }

        public void setProductQuantity(int productQuantity) {
            this.productQuantity = productQuantity;
        }

        public void setShopRating(float shopRating) {
            this.shopRating = shopRating;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }
    }
}
