package com.example.foodeli.MySqlSetUp;

import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserProfile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pool {

    private Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ducanh2611-001-site1.gtempurl.com/api/v2/index.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    private RetrofitAPICallUserProfile retrofitAPI = retrofit.create(RetrofitAPICallUserProfile.class);

    public RetrofitAPICallUserProfile getPool() {
        return this.retrofitAPI;
    }

}
