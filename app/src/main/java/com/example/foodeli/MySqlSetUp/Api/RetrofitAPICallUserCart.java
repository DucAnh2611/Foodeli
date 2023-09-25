package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.Cart.Body.AddToCartBody;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.AddToCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.DeleteItemRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.UpdateItemRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallUserCart {

    String base = "user/cart/";

    @POST(base + "add")
    Call<AddToCartRes> addItemToCart(@Body AddToCartBody body);

    @GET(base + "get")
    Call<GetCartRes> getCartUser(@Query("uid") int uid);

    @PUT(base + "update-item")
    Call<UpdateItemRes> updateItem(@Body AddToCartBody body);

    @DELETE(base + "delete-item")
    Call<DeleteItemRes> deleteItem(@Query("uid") int uid, @Query("pid") int pid);

    @DELETE(base + "delete-cart")
    Call<DeleteItemRes> deleteCart(@Query("uid") int uid);

}

