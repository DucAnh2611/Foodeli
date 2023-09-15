package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CancelReasonRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.MethodSupportRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.OrderStateRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.ShopPermissionRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPICallGeneral {

    String base ="general/get/";

    @GET(base + "category") Call<CategoryRes> getSystemCategory(@Query("limit") int limit);

    @GET(base + "method") Call<MethodSupportRes> getSystemMethod();

    @GET(base + "order-state") Call<OrderStateRes> getSystemState();

    @GET(base + "shop-permission") Call<ShopPermissionRes> getSystemShopPermission();

    @GET(base + "cancel-reason") Call<CancelReasonRes> getSystemCancelReason();


}
