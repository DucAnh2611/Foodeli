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

    public String getName() {
        return name;
    }

    public int getSid() {
        return sid;
    }

    public float getPrice() {
        return price;
    }

    public float getStock() {
        return stock;
    }

    public int getDelete() {
        return delete;
    }

    public int getPid() {
        return pid;
    }

    public String getCreate() {
        return create;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public String getModified() {
        return modified;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getUnit() {
        return unit;
    }
}
