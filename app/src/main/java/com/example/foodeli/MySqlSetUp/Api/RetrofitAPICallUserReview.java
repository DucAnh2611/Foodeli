package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Body.CreateReviewBody;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.CreateReviewRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPICallUserReview {

    String base = "user/product-review/";

    @POST(base + "create")
    Call<CreateReviewRes> createReview(@Body CreateReviewBody body);


}
