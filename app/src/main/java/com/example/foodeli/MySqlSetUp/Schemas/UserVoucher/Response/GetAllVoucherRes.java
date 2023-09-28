package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllVoucherRes extends ResponseApi {

    @SerializedName("vouchers")
    private ArrayList<VoucherWithRemain> vouchers;

    public class VoucherWithRemain extends Voucher{
        @SerializedName("VoucherRemain")
        private int remain;

        public int getRemain() {
            return remain;
        }
    }

    public ArrayList<VoucherWithRemain> getVouchers() {
        return vouchers;
    }
}
