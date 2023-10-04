package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

public class OrderWithState extends Order{

    @SerializedName("StateContent")
    private String state;

    @SerializedName("OrderItemCount")
    private int itemCount;

    public int getItemCount() {
        return itemCount;
    }

    public String getState() {
        return state;
    }
}
