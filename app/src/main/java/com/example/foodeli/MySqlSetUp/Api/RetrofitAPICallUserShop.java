package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.CreateShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.MangerUserBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.UpdateShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.CreateShopResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllProductShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetShopInformationResponse;
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

    @GET(base + "method?type=info-shop")
    Call<GetShopInformationResponse> getShop(@Query("id") int sid);

    @GET(base + "method?type=info-product")
    Call<GetAllProductShop> getShopProduct(@Query("id") int sid);

    @PUT(base + "method")
    Call<GetShopInformationResponse> updateShop(@Query("type") String type, @Body UpdateShopBody body);

    @POST(base + "manageUser")
    Call<MangeUserRes> createEmployee(@Body MangerUserBody body);

    @GET(base + "manageUser")
    Call<GetAllUserInShop> getEmployee(@Query("sid") int sid);

    @PUT(base + "manageUser")
    Call<MangeUserRes> updateEmployeePermission(@Body MangerUserBody body);

    @DELETE(base + "manageUser")
    Call<MangeUserRes> deleteEmployee(@Body MangerUserBody body);

}
