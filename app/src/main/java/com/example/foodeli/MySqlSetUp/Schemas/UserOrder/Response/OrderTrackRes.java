package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Order;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderTrackRes extends ResponseApi {

    @SerializedName("info")
    private Order order;

    @SerializedName("items")
    private ArrayList<OrderItems> list;

    public class OrderItems{

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
    }

    public Order getOrder() {
        return order;
    }

    public ArrayList<OrderItems> getList() {
        return list;
    }
}
