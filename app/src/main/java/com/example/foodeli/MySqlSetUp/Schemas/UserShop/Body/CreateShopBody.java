package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body;

public class CreateShopBody {
    private String name, desc, address, avatar;
    private int uid;

    public CreateShopBody(String name, String desc, String address, int uid, String avatar) {
        this.avatar = avatar;
        this.uid = uid;
        this.name = name;
        this.desc = desc;
        this.address = address;
    }
}
