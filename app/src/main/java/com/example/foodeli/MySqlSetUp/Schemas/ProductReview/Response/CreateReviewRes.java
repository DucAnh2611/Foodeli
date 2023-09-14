package com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response;

import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.google.gson.annotations.SerializedName;

public class CreateReviewRes extends ResponseApi {

    @SerializedName("saved")
    private boolean saved;

    public boolean getSaved() {
        return this.saved;
    }
}
