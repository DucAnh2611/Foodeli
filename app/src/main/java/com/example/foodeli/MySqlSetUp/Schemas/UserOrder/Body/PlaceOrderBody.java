package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body;

public class PlaceOrderBody {

    private int uid, addid, ckid, vid;
    private String uname, phone, email;

    public PlaceOrderBody(
            int uid,
            int addid,
            int ckid,
            int vid,
            String uname,
            String phone,
            String email
    ) {
        this.uid = uid;
        this.addid = addid;
        this.ckid = ckid;
        this.vid = vid;
        this.uname = uname;
        this.phone = phone;
        this.email = email;
    }

}
