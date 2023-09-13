package com.example.foodeli.MySqlSetUp.Schemas.Address;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("id")
    private int aid;
    @SerializedName("user_id")
    private int uid;
    @SerializedName("address")
    private String address;
    @SerializedName("Deleted")
    private int deleted = 0;

    public Address(int aid, int uid, String address) {
        this.aid = aid;
        this.uid= uid;
        this.address = address;
    }

}
