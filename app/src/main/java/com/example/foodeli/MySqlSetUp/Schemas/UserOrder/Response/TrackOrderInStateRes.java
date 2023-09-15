package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrackOrderInStateRes extends ResponseApi {

    @SerializedName("order")
    private ArrayList<OrderWithState> orders;

    public ArrayList<OrderWithState> getOrders() {
        return this.orders;
    }
}
