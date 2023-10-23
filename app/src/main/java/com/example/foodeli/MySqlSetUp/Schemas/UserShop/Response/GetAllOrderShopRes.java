package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response;

import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllOrderShopRes implements Serializable {

    @SerializedName("shop")
    private ArrayList<OrderWithState> orders;

    public ArrayList<OrderWithState> getOrders() {
        return orders;
    }
}
