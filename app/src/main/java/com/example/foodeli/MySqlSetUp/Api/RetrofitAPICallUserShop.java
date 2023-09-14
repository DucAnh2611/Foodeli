package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.CreateShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.MangerUserBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.CreateShopResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetShopInfomationResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.MangeUserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallUserShop {

    String base = "user/shop/";

    @POST(base + "method")
    Call<CreateShopResponse> createShop(@Body CreateShopBody shop);

    @GET(base + "method?type=all")
    Call<GetAllShopUserHaveResponse> getAllShopOfUser(@Query("id") int uid);

    @GET(base + "method?type=info")
    Call<GetShopInfomationResponse> getShop(@Query("id") int sid);

    @PUT(base + "method")
    Call<GetShopInfomationResponse> updateShop(@Query("type") String type, @Query("id") int sid, @Body CreateShopBody updateInfo);

    @POST(base + "manageUser")
    Call<MangeUserRes> createEmployee(@Body MangerUserBody body);

    @PUT(base + "manageUser")
    Call<MangeUserRes> updateEmployeePermission(@Body MangerUserBody body);

    @DELETE(base + "manageUser")
    Call<MangeUserRes> deleteEmployee(@Body MangerUserBody body);

}
