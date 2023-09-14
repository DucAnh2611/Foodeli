package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body;

public class CreateShopBody {
    private String name, desc, address;
    private int uid;

    public CreateShopBody(String name, String desc, String address, int uid) {
        this.uid = uid;
        this.name = name;
        this.desc = desc;
        this.address = address;
    }
}
