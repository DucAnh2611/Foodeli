package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Body.CreateReviewBody;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.CreateReviewRes;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.GetProductReviewRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPICallUserReview {

    String base = "user/product-review/";

    @POST(base + "create")
    Call<CreateReviewRes> createReview(@Body CreateReviewBody body);

    @GET(base + "get")
    Call<GetProductReviewRes> getReviewOfProduct(@Query("pid") int pid, @Query("rate") int rate, @Query("page") int page, @Query("limit") int limit);

}
