package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body;

public class PlaceOrderBody {

    private int uid, addid, ckid, vid, payed;
    private String uname, phone, email;
    private double total;

    public PlaceOrderBody(
            int uid,
            int addid,
            int ckid,
            int vid,
            String uname,
            String phone,
            String email,
            double total,
            int payed
    ) {
        this.uid = uid;
        this.addid = addid;
        this.ckid = ckid;
        this.vid = vid;
        this.uname = uname;
        this.phone = phone;
        this.email = email;
        this.total = total;
        this.payed = payed;
    }

}
