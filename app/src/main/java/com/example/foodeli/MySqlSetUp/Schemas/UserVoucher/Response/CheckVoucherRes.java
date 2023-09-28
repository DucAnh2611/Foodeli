package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class CheckVoucherRes extends ResponseApi {

    @SerializedName("discount")
    private Discount discount;

    public class Discount {
        @SerializedName("OrderTotal")
        private double total;

        @SerializedName("OrderShippingFee")
        private double shipping;

        public double getShipping() {
            return shipping;
        }

        public double getTotal() {
            return total;
        }
    }

    public Discount getDiscount() {
        return discount;
    }
}
