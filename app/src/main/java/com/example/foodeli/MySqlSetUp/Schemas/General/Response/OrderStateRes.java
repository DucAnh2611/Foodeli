package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.OrderState;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderStateRes extends ResponseApi {

    @SerializedName("state") private ArrayList<OrderState> state;

    public ArrayList<OrderState> getState() {
        return state;
    }
}
