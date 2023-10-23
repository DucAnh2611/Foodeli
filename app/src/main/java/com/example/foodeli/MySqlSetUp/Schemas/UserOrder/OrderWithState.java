package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderWithState extends Order implements Serializable {

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

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setState(String state) {
        this.state = state;
    }
}
