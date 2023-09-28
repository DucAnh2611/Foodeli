package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body.SaveVoucherBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body.CheckCanUseVoucher;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.CheckVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.FindVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.SaveVoucherRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPICallUserVoucher {

    String base = "user/voucher/";

    @GET(base + "find")
    Call<FindVoucherRes> findVoucher(@Query("key") String key);

    @GET(base + "getAll")
    Call<GetAllVoucherRes> getAllVoucher(@Query("uid") int uid);

    @POST(base + "save")
    Call<SaveVoucherRes> saveVoucher(@Body SaveVoucherBody body);

    @POST(base + "check")
    Call<CheckVoucherRes> checkCanUseVoucher(@Body CheckCanUseVoucher body);

}
