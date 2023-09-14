package com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class DeleteVoucherRes extends ResponseApi {

    @SerializedName("delete")
    private boolean delete;

    public boolean getDelete() {
        return this.delete;
    }
}
