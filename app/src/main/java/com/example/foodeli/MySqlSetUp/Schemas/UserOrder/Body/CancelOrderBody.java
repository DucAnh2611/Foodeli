package com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body;

public class CancelOrderBody {

    private int oid, uid, rid;
    private String rElse, rDetail;

    public CancelOrderBody(int oid, int uid, int rid, String rElse, String rDetail){
        this.uid = uid;
        this.oid = oid;
        this.rid = rid;
        this.rElse = rElse;
        this.rDetail = rDetail;
    }
}
