package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class CheckVoucherRes extends ResponseApi {

    @SerializedName("discount")
    private Discount discount;

    public class Discount {
        @SerializedName("OrderTotal")
        private float total;

        @SerializedName("OrderShipping")
        private float shipping;
    }

}
