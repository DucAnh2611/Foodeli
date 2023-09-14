package com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class SaveVoucherRes extends ResponseApi {

    @SerializedName("saved")
    private boolean saved = false;

}
