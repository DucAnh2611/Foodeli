package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderRes extends ResponseApi {

    @SerializedName("orderId")
    private int orderId;

    public int getOrderId() {
        return orderId;
    }
}
