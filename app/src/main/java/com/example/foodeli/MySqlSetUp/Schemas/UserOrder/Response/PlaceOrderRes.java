package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderRes extends ResponseApi {

    @SerializedName("order")
    private OrderWithState order;

    public OrderWithState getOrder() {
        return order;
    }
}
