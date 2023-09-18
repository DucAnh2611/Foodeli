package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body;

public class CheckCanUseVoucher {
    private float total, shipping;
    private int vid;

    public CheckCanUseVoucher(float total, float shipping, int vid) {
        this.total = total;
        this.shipping = shipping;
        this.vid = vid;
    }
}
