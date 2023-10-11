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
    private int deleted;

    public Address(int aid, int uid, String address, int deleted) {
        this.aid = aid;
        this.uid= uid;
        this.address = address;
        this.deleted = deleted;
    }

    public int getUid() {
        return uid;
    }

    public int getAid() {
        return aid;
    }

    public int getDeleted() {
        return deleted;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
