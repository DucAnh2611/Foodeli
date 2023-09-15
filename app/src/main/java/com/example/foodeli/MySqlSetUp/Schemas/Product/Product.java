package com.example.foodeli.MySqlSetUp.Schemas.Product;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ProductID")
    private int pid;
    @SerializedName("ShopId")
    private int sid;
    @SerializedName("ProductName")
    private String name;
    @SerializedName("ProductPrice")
    private float price;
    @SerializedName("ProductUnit")
    private String unit;
    @SerializedName("ProductShortDesc")
    private String shortDesc;
    @SerializedName("ProductLongDesc")
    private String longDesc;
    @SerializedName("ProductTimeStart")
    private String timeStart;
    @SerializedName("ProductTimeFinish")
    private String timeFinish;
    @SerializedName("ProductCreateAt")
    private String create;
    @SerializedName("ProductLastModified")
    private String modified;
    @SerializedName("ProductStock")
    private float stock;
    @SerializedName("Deleted")
    private int delete;

}
