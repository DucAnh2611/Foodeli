package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body;

public class PlaceOrderBody {

    private int uid, ckid, payed, vid;
    private String uname, phone, email, address;
    private double total, discount, shipping, tax;

    public PlaceOrderBody(int uid, int ckid, int vid, int payed, String uname, String phone, String email, String address, double total, double discount, double shipping, double tax) {
        this.uid = uid;
        this.ckid = ckid;
        this.vid = vid;
        this.payed = payed;
        this.uname = uname;
        this.phone = phone;
        this.email = email;
        this.total = total;
        this.discount = discount;
        this.shipping = shipping;
        this.tax = tax;
        this.address = address;
    }
}
