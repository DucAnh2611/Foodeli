package com.example.foodeli.MySqlSetUp.Schemas.Method;

import com.google.gson.annotations.SerializedName;

public class Method {

    @SerializedName("UserMethodId")
    private int id;
    @SerializedName("UserId")
    private int uid;
    @SerializedName("MethodId")
    private int mid;
    @SerializedName("UserMethodNumber")
    private String number;
    @SerializedName("UserMethodExpireAt")
    private String expired;
    @SerializedName("UserMethodDesc")
    private String desc;
    @SerializedName("Deleted")
    private int delete;

    public Method(
            int id,
            int uid,
            int mid,
            String number,
            String expired,
            String desc,
            int delete
    ) {
        this.id = id;
        this.uid = uid;
        this.mid = mid;
        this.number = number;
        this.expired = expired;
        this.desc= desc;
        this.delete = delete;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public int getUid() {
        return uid;
    }

    public int getDelete() {
        return delete;
    }

    public int getMid() {
        return mid;
    }

    public String getExpired() {
        return expired;
    }

    public String getNumber() {
        return number;
    }
}
