package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class OrderState {
    @SerializedName("StateId") private int stId;
    @SerializedName("StateContent") private String content;
}
