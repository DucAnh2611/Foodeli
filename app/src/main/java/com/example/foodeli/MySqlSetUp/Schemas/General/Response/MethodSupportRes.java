package com.example.foodeli.MySqlSetUp.Schemas.General.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MethodSupportRes extends ResponseApi {

    @SerializedName("method") private ArrayList<MethodSupport> method;

    public ArrayList<MethodSupport> getMethod() {
        return method;
    }

}
