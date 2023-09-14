package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Body.VoucherShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.CreateVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.DeleteVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.GetAllVoucherShopRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallShopVoucher {

    String base = "shop/voucher/";

    @GET(base + "get")
    Call<GetAllVoucherShopRes> getAlVoucherShop(@Query("sid") int sid);

    @POST(base + "create")
    Call<CreateVoucherRes> createVoucher(@Body VoucherShopBody body);

    @PUT(base + "update")
    Call<CreateVoucherRes> updateVoucher(@Body VoucherShopBody body);

    @DELETE(base + "delete")
    Call<DeleteVoucherRes> deleteVoucher(@Query("uid") int uid, @Query("vid") int vid, @Query("sid") int sid );


}
