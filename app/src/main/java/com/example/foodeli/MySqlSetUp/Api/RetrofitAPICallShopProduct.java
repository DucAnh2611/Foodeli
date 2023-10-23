package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.CreateProductRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.DeleteInfoRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.GetProductInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPICallShopProduct {

    String base = "shop/product/";

    @GET(base + "search")
    Call<GetTopProduct> findProduct(
            @Query("key") String key, @Query("min") float min, @Query("max") float max,
            @Query("cid") int cid, @Query("page") int page
    );

    @GET(base + "getInfo")
    Call<GetProductInfo> getProductInfo(@Query("pid") int pid);

    @GET(base + "product-info")
    Call<CreateProductRes> getProductData(@Query("pid") int pid);

    @POST(base + "new")
    Call<CreateProductRes> newProduct(@Body UpdateProductBody body);

    @POST(base + "update-info")
    Call<CreateProductRes> updateProductData(@Body UpdateProductBody body, @Query("pid") int pid);

    @DELETE(base + "delete?type=data")
    Call<DeleteInfoRes> deleteProduct(@Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid);

    @DELETE(base + "delete?type=image")
    Call<DeleteInfoRes> deleteProductImage(
            @Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid, @Query("iid") int iid
    );
    @DELETE(base + "delete?type=category")
    Call<DeleteInfoRes> deleteProductCategory(
            @Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid, @Query("cid") int cid
    );


}
