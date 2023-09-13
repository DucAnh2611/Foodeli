package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Body.CreateAddress;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.DeleteAddressRes;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.GetAllAddressRes;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallUserAddress {

    String base = "user/address/";

    @POST(base + "new")
    Call<CreateAddress> createAddress(@Body CreateAddress address);

    @GET(base + "getAll")
    Call<GetAllAddressRes> getAll(@Query("id") int uid);

    @DELETE(base + "delete")
    Call<DeleteAddressRes> deleteAddress(@Query("id") int aid);

    @PUT(base + "update")
    Call<CreateAddress> updateAddress(@Query("id") int uid, @Query("addressId") int aid, @Body Address address);

}
