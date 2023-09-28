package com.example.foodeli.MySqlSetUp.Schemas.Cart.Body;

public class AddToCartBody {

    private int uid, pid;
    private int quantity;

    public AddToCartBody(int uid, int pid, int quantity) {
        this.uid = uid;
        this.pid = pid;
        this.quantity = quantity;
    }

}
