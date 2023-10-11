package com.example.foodeli.MySqlSetUp.Schemas.UserShop;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shop implements Serializable {

    @SerializedName("ShopId")
    private int sid;
    @SerializedName("ShopAvatar")
    private String avatar;
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

    public int getSid() {
        return sid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getAddress() {
        return address;
    }

    public String getCreate() {
        return create;
    }

    public String getModified() {
        return modified;
    }

    public String getDeleted() {
        return deleted;
    }
}
