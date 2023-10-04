package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body;

public class CancelOrderBody {

    private int oid, uid, rid;
    private String desc;

    public CancelOrderBody(int oid, int uid, int rid, String desc){
        this.uid = uid;
        this.oid = oid;
        this.rid = rid;
        this.desc = desc;
    }
}
