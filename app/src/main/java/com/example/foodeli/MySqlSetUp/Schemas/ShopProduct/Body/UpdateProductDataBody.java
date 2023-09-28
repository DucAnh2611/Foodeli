package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

public class UpdateProductDataBody extends ProductPostBody{

    private int pid;

    public UpdateProductDataBody(
            int pid,
            int uid,
            int sid,
            float price,
            int stock,
            String name,
            String unit,
            String shortDesc,
            String longDesc
    ) {
        super(uid, sid, price, stock, name, unit, shortDesc, longDesc);
        this.pid = pid;
    }
}
