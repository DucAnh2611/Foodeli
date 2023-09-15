package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

public class OrderWithState extends Order{

    @SerializedName("StateContent")
    private String state;

}
