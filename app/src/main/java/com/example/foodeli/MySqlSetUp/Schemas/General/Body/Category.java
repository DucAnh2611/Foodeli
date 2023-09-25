package com.example.foodeli.MySqlSetUp.Schemas.General.Body;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("CategoryID") private int cid;
    @SerializedName("CategoryName") private String name;
    @SerializedName("CategoryImage") private String image;
    @SerializedName("CategoryParentId") private int p_cid = 0;

    public String getName() {
        return name;
    }

    public int getCid() {
        return cid;
    }

    public String getImage() {
        return image;
    }

    public int getP_cid() {
        return p_cid;
    }
}
