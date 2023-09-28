package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body;

public class CheckCanUseVoucher {
    private double total, shipping;
    private int vid;

    public CheckCanUseVoucher(double total, double shipping, int vid) {
        this.total = total;
        this.shipping = shipping;
        this.vid = vid;
    }
}
