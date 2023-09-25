package com.example.foodeli.MySqlSetUp;

import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallGeneral;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallShopProduct;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallShopVoucher;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserAddress;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserCart;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserMethod;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserOrder;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserProfile;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserReview;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserShop;
import com.example.foodeli.MySqlSetUp.Api.RetrofitAPICallUserVoucher;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pool {

    private Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ducanh2611-001-site1.gtempurl.com/api/v2/index.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    private RetrofitAPICallUserProfile ApiCallUserProfile = retrofit.create(RetrofitAPICallUserProfile.class);
    private RetrofitAPICallGeneral ApiCallGeneral = retrofit.create(RetrofitAPICallGeneral.class);
    private RetrofitAPICallShopProduct ApiCallShopProduct = retrofit.create(RetrofitAPICallShopProduct.class);
    private RetrofitAPICallShopVoucher ApiCallShopVoucher = retrofit.create(RetrofitAPICallShopVoucher.class);
    private RetrofitAPICallUserAddress ApiCallUserAddress = retrofit.create(RetrofitAPICallUserAddress.class);

    private RetrofitAPICallUserCart ApiCallUserCart = retrofit.create(RetrofitAPICallUserCart.class);
    private RetrofitAPICallUserMethod ApiCallUserMethod = retrofit.create(RetrofitAPICallUserMethod.class);
    private RetrofitAPICallUserOrder ApiCallUserOrder = retrofit.create(RetrofitAPICallUserOrder.class);
    private RetrofitAPICallUserReview ApiCallUserReview = retrofit.create(RetrofitAPICallUserReview.class);
    private RetrofitAPICallUserShop ApiCallUserShop = retrofit.create(RetrofitAPICallUserShop.class);
    private RetrofitAPICallUserVoucher ApiCallUserVoucher = retrofit.create(RetrofitAPICallUserVoucher.class);

    public RetrofitAPICallUserProfile getApiCallUserProfile() {
        return this.ApiCallUserProfile;
    }

    public RetrofitAPICallGeneral getApiCallGeneral() {
        return ApiCallGeneral;
    }

    public RetrofitAPICallShopProduct getApiCallShopProduct() {
        return ApiCallShopProduct;
    }

    public RetrofitAPICallShopVoucher getApiCallShopVoucher() {
        return ApiCallShopVoucher;
    }

    public RetrofitAPICallUserAddress getApiCallUserAddress() {
        return ApiCallUserAddress;
    }

    public RetrofitAPICallUserCart getApiCallUserCart() {return ApiCallUserCart;}

    public RetrofitAPICallUserMethod getApiCallUserMethod() {
        return ApiCallUserMethod;
    }

    public RetrofitAPICallUserOrder getApiCallUserOrder() {
        return ApiCallUserOrder;
    }

    public RetrofitAPICallUserReview getApiCallUserReview() {
        return ApiCallUserReview;
    }

    public RetrofitAPICallUserShop getApiCallUserShop() {
        return ApiCallUserShop;
    }

    public RetrofitAPICallUserVoucher getApiCallUserVoucher() {
        return ApiCallUserVoucher;
    }
}
