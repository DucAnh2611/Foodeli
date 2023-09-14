package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body;

public class SaveVoucherBody {

    private int uid;
    private String type, data;

    public SaveVoucherBody(int uid, String type, String data) {
        this.uid = uid;
        this.type = type;
        this.data = data;
    }

}
