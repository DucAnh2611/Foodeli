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
    private int stock;
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

    public int getStock() {
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

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
