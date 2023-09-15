package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body;

public class UpdateShopBody {

    private int uid, sid;
    private String field, data;

    public UpdateShopBody(int uid, int sid, String field, String data) {
        this.uid = uid;
        this.sid = sid;
        this.field = field;
        this.data = data;
    }


}
