package com.example.foodeli.MySqlSetUp.Api;


import com.example.foodeli.MySqlSetUp.Schemas.User.Body.ChangeForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.CreateAccount;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.SendOTPBody;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.CreateUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.FindUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.GetUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.LoginForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.LoginRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.SendOTPResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallUserProfile {

    @GET("user/profile/get")
    Call<GetUserResponse> getUserInfoById(@Query("id") int id);

    @POST("user/profile/signup")
    Call<CreateUserResponse> signup(@Body CreateAccount newAccount);

    @POST("user/profile/login")
    Call<LoginRes> login(@Body LoginForm user);

    @PUT("user/profile/change")
    Call<CreateUserResponse> update(@Query("id") int id, @Body ArrayList<ChangeForm> changeForm);

    @POST("user/profile/send-otp")
    Call<SendOTPResponse> sendOTP(@Body SendOTPBody body);

    @GET("user/profile/find-user")
    Call<FindUserResponse> findUser(@Query("key") String key);


}