package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.UpdateItemRes;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallShopOrder {

    String base = "shop/order/";

    @PUT(base + "update")
    Call<UpdateItemRes> updateOrder(@Query("sid") int sid, @Query("uid") int uid, @Query("oid") int oid, @Query("osid") int osid);

}
