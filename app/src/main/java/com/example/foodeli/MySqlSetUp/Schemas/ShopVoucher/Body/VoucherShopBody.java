package com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Body;

public class VoucherShopBody {

    private int uid, sid, expired, limit;
    private String text, title, desc, code;
    private float min, max, discount;

    public VoucherShopBody(
        int uid,
        int sid,
        int expired,
        int limit,
        String text,
        String title,
        String desc,
        String code,
        float min,
        float max,
        float discount
    ) {
        this.uid = uid;
        this.sid = sid;
        this.expired = expired;
        this.limit = limit;
        this.text = text;
        this.title = title;
        this.desc = desc;
        this.code = code;
        this.min = min;
        this.max = max;
        this.discount = discount;
    }

}
