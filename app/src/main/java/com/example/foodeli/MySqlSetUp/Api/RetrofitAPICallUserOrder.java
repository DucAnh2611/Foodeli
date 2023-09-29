package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body.CancelOrderBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body.PlaceOrderBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.CancelRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.PlaceOrderRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.TrackOrderInStateRes;

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

    @GET(base + "track-state")
    Call<TrackOrderInStateRes> listOrderInState(@Query("uid") int uid, @Query("type") String type);

    @POST(base + "cancel")
    Call<CancelRes> cancelOrder(@Body CancelOrderBody body);
}
