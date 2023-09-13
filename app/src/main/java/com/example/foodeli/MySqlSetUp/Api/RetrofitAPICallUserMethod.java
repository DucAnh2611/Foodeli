package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.GetAllMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Response.CreateMethodRes;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPICallUserMethod {

    String base = "user/method/";

    @POST(base + "create")
    Call<CreateMethodRes> createMethod(@Body CreateMethod method);

    @DELETE(base + "delete")
    Call<Response> deleteMethod(@Query("id") int id);

    @GET(base + "getAll")
    Call<GetAllMethod> getAllMethod(@Query("id") int id);


}
