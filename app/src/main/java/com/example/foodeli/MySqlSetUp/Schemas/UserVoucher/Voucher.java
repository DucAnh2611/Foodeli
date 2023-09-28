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
    private String title;
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

    @SerializedName("VoucherTarget")
    private String target;
    @SerializedName("VoucherExpired")
    private int expired;
    @SerializedName("Deleted")
    private int deleted;

    public float getDiscount() {
        return discount;
    }

    public int getDeleted() {
        return deleted;
    }

    public String getDesc() {
        return desc;
    }

    public String getCreate() {
        return create;
    }

    public int getId() {
        return id;
    }

    public String getModified() {
        return modified;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public int getExpired() {
        return expired;
    }

    public int getLimit() {
        return limit;
    }

    public int getOwner() {
        return owner;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getTarget() {
        return target;
    }
}
