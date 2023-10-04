package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class CheckVoucherRes extends ResponseApi {

    @SerializedName("discount")
    private Discount discount;

    public class Discount {

        @SerializedName("OrderTax") private double tax;
        @SerializedName("OrderDiscount") private double discount;
        @SerializedName("OrderTotal") private double total;

        public double getTax() {
            return tax;
        }

        public double getDiscount() {
            return discount;
        }

        public double getTotal() {
            return total;
        }

    }

    public Discount getDiscount() {
        return discount;
    }
}
