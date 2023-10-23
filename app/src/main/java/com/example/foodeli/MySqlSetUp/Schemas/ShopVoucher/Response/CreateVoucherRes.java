package com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response;

import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.annotations.SerializedName;

public class CreateVoucherRes {

    @SerializedName("voucher")
    private Voucher voucher;

    public Voucher getVoucher() {
        return voucher;
    }
}
