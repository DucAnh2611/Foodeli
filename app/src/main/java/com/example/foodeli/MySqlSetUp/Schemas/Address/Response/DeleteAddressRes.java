package com.example.foodeli.MySqlSetUp.Schemas.Address.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class DeleteAddressRes extends ResponseApi {

    @SerializedName("delete")
    private boolean delete;

    public boolean getDelete() {return this.delete;}

}
