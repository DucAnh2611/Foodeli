package com.example.foodeli.MySqlSetUp.Schemas.Cart.Body;

public class AddToCartBody {

    private int uid, pid;
    private float quantity;

    public AddToCartBody(int uid, int pid, float quantity) {
        this.uid = uid;
        this.pid = pid;
        this.quantity = quantity;
    }

}
