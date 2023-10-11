package com.example.foodeli.MySqlSetUp.Schemas.Method;

import com.google.gson.annotations.SerializedName;

public class MethodWithTypeName extends Method {

    @SerializedName("MethodType")
    private String type;

    public MethodWithTypeName(
            int id,
            int uid,
            int mid,
            String number,
            String expired,
            String desc,
            int delete,
            String type
    ) {
        super(id, uid, mid, number, expired, desc, delete);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
