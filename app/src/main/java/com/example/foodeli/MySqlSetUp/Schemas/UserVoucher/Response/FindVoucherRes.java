package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FindVoucherRes extends ResponseApi {

    @SerializedName("voucher")
    private ArrayList<Voucher> list;

    public ArrayList<Voucher> getList() {
        return list;
    }
}
