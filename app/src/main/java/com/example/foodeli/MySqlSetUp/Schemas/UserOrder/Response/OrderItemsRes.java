package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItemsRes implements Serializable {

    @SerializedName("items")
    private ArrayList<OrderItems> items;

    public static class OrderItems implements Serializable  {

        @SerializedName("ProductID")
        private int pid;
        @SerializedName("ProductName")
        private String pname;
        @SerializedName("ProductPrice")
        private float price;
        @SerializedName("ProductQuantity")
        private float quantity;
        @SerializedName("ProductUnit")
        private String unit;
        @SerializedName("ProductImage")
        private ProductImage image;

        public int getPid() {
            return pid;
        }

        public String getPname() {
            return pname;
        }

        public float getPrice() {
            return price;
        }

        public float getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }

        public ProductImage getImage() {
            return image;
        }
    }

    public static class ProductImage implements Serializable {
        @SerializedName("id") private int productImageId;
        @SerializedName("base64") private String base64Image;

        public int getProductImageId() {
            return productImageId;
        }

        public String getBase64Image() {
            return base64Image;
        }
    }

    public ArrayList<OrderItems> getItems() {
        return items;
    }
}
