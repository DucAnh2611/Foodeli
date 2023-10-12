package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("OrderID")
    private int oid;
    @SerializedName("OrderUserID")
    private int uid;
    @SerializedName("OrderShipAddress")
    private String address;
    @SerializedName("OrderDiscount")
    private double discount;
    @SerializedName("CheckoutMethodId")
    private int ckid;
    @SerializedName("OrderShipName")
    private String name;
    @SerializedName("OrderPhone")
    private String phone;
    @SerializedName("OrderEmail")
    private String email;
    @SerializedName("CreateAt")
    private String create;
    @SerializedName("Modified")
    private String modified;
    @SerializedName("OrderTotal")
    private float total;
    @SerializedName("Payed")
    private int payed;
    @SerializedName("OrderTax")
    private float tax;
    @SerializedName("OrderShippingFee")
    private float shipFee;
    @SerializedName("OrderStateId")
    private int stateId;

    public int getOid() {
        return oid;
    }

    public int getUid() {
        return uid;
    }

    public String getAddress() {
        return address;
    }

    public double getDiscount() {
        return discount;
    }

    public int getCkid() {
        return ckid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCreate() {
        return create;
    }

    public String getModified() {
        return modified;
    }

    public float getTotal() {
        return total;
    }

    public int getPayed() {
        return payed;
    }

    public float getTax() {
        return tax;
    }

    public float getShipFee() {
        return shipFee;
    }

    public int getStateId() {
        return stateId;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
