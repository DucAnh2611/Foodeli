package com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllVoucherShopRes extends ResponseApi {

    @SerializedName("items")
    private ArrayList<Voucher> items;

    public ArrayList<Voucher> getItems() {
        return items;
    }
}
