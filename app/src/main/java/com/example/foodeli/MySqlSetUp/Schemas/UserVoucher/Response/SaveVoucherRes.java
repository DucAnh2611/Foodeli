package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SaveVoucherRes extends ResponseApi {

    @SerializedName("saved")
    private ArrayList<GetAllVoucherRes.VoucherWithRemain> voucher;

    public ArrayList<GetAllVoucherRes.VoucherWithRemain> getVoucher() {
        return voucher;
    }
}
