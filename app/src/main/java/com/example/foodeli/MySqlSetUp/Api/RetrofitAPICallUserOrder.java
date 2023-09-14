package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body.PlaceOrderBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.PlaceOrderRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPICallUserOrder {

    String base = "user/order/";

    @POST(base + "place")
    Call<PlaceOrderRes> placeOrder(@Body PlaceOrderBody body);

    @GET(base + "track")
    Call<PlaceOrderRes> trackingOrder(@Query("oid") int oid, @Query("uid") int uid);

}
