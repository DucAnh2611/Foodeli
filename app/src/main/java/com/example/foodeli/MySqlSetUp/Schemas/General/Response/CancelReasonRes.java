package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.CancelReason;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CancelReasonRes extends ResponseApi {

    @SerializedName("reason") private ArrayList<CancelReason> reasons;

    public ArrayList<CancelReason> getReasons() {
        return reasons;
    }
}
