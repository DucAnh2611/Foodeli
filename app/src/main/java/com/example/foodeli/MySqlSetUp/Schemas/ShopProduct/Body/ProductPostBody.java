package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

public class ProductPostBody {
    private int uid, sid, stock, from, to;
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
            String longDesc,
            int from,
            int to
    ) {
        this.uid = uid;
        this.sid = sid;
        this.price = price;
        this.stock = stock;
        this.name = name;
        this.unit = unit;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.from = from;
        this.to = to;
    }

    public int getSid() {
        return this.sid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getStock() {
        return stock;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }
}
