package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("OrderID")
    private int oid;
    @SerializedName("OrderUserID")
    private int uid;
    @SerializedName("ShopId")
    private int sid;
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

    public int getSid() {
        return sid;
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

    public void setOid(int oid) {
        this.oid = oid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setCkid(int ckid) {
        this.ckid = ckid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public void setShipFee(float shipFee) {
        this.shipFee = shipFee;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}
