package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class CancelReason {

    @SerializedName("ReasonId") private int rcId;

    @SerializedName("ReasonName") private String rcName;
}
