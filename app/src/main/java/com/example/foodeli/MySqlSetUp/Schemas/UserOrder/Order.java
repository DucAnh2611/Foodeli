package com.example.foodeli.MySqlSetUp.Schemas.UserOrder;

import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("OrderID")
    private int oid;
    @SerializedName("OrderUserID")
    private int uid;
    @SerializedName("OrderShipAddressId")
    private int addid;
    @SerializedName("VoucherId")
    private int vid;
    @SerializedName("OrderStateId")
    private int stateId;
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
    @SerializedName("OrderShipped")
    private int shipped;
    @SerializedName("Confirm")
    private int confirm;
    @SerializedName("OrderTax")
    private float tax;
    @SerializedName("OrderShippingFee")
    private float shipFee;
    @SerializedName("OrderTotal")
    private float total;

}
