package com.example.foodeli.MySqlSetUp.Schemas.Method.Body;

import com.example.foodeli.MySqlSetUp.Response;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllMethod extends Response {

    @SerializedName("method")
    private ArrayList<MethodWithTypeName> list;

}
