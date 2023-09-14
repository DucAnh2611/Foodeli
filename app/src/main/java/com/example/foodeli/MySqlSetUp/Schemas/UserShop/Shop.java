package com.example.foodeli.MySqlSetUp.Schemas.UserShop;

import com.google.gson.annotations.SerializedName;

public class Shop {

    @SerializedName("ShopId")
    private int sid;
    @SerializedName("name")
    private String name;
    @SerializedName("desc")
    private String desc;
    @SerializedName("address")
    private String address;
    @SerializedName("create_at")
    private String create;
    @SerializedName("modified_at")
    private String modified;
    @SerializedName("deleted_at")
    private String deleted;



}
