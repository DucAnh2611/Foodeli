package com.example.foodeli.MySqlSetUp.Schemas.Method.Body;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllMethod extends ResponseApi {

    @SerializedName("method")
    private ArrayList<MethodWithTypeName> list;

    public ArrayList<MethodWithTypeName> getList() {
        return list;
    }
}
