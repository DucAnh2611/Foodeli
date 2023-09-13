package com.example.foodeli.MySqlSetUp.Schemas.Method;

import com.example.foodeli.MySqlSetUp.Schemas.Method.Method;
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
}
