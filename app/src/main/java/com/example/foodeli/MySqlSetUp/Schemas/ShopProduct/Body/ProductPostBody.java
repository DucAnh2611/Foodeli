package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

public class ProductPostBody {
    private int uid, sid, stock;;
    private float price;
    private String name, unit, shortDesc, longDesc;

    public ProductPostBody(
            int uid,
            int sid,
            float price,
            int stock,
            String name,
            String unit,
            String shortDesc,
            String longDesc
    ) {
        this.uid = uid;
        this.sid = sid;
        this.price = price;
        this.stock = stock;
        this.name = name;
        this.unit = unit;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public int getSid() {
        return this.sid;
    }

    public int getUid() {
        return this.uid;
    }
}
