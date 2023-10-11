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
        private String image;
        @SerializedName("CanReview")
        private int canReview;

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

        public String getImage() {
            return image;
        }

        public int getCanReview() {
            return canReview;
        }
    }

    public ArrayList<OrderItems> getItems() {
        return items;
    }
}
