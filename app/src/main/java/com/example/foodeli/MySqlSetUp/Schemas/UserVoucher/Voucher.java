package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher;

import com.google.gson.annotations.SerializedName;

public class Voucher {

    @SerializedName("VoucherId")
    private int id;
    @SerializedName("VoucherCode")
    private String code;
    @SerializedName("VoucherText")
    private String text;
    @SerializedName("VoucherTitle")
    private String tile;
    @SerializedName("VoucherDesc")
    private String desc;
    @SerializedName("VoucherMinPrice")
    private float min;
    @SerializedName("VoucherMaxDiscount")
    private float max;
    @SerializedName("VoucherDiscount")
    private float discount;
    @SerializedName("VoucherLimit")
    private int limit;
    @SerializedName("VoucherCreateAt")
    private String create;
    @SerializedName("VoucherLastModified")
    private String modified;
    @SerializedName("VoucherShopOwner")
    private int owner;
    @SerializedName("VoucherExpired")
    private int expired;
    @SerializedName("Deleted")
    private int deleted;

}
