package com.example.foodeli.MySqlSetUp.Schemas.Method.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Method;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.google.gson.annotations.SerializedName;

public class CreateMethodRes extends ResponseApi {

    @SerializedName("method")
    private MethodWithTypeName method;

    public MethodWithTypeName getMethod() {return this.method;}


}
