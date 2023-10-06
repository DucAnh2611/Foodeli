package com.example.foodeli.MySqlSetUp.Api;

import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.CreateProductBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductCategoryBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductDataBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body.UpdateProductImageBody;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.CreateProductRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.DeleteInfoRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.FindProductRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.GetProductInfo;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST(base + "new")
    Call<CreateProductRes> newProduct(@Body CreateProductBody body);

    @PUT(base + "update?type=data")
    Call<CreateProductRes> updateProductData(@Body UpdateProductDataBody body);

    @PUT(base + "update?type=image")
    Call<CreateProductRes> updateProductImage(@Body UpdateProductImageBody body);

    @PUT(base + "update?type=category")
    Call<CreateProductRes> updateProductCategory(@Body UpdateProductCategoryBody body);

    @PUT(base + "delete?type=data")
    Call<DeleteInfoRes> deleteProduct(@Query("type") String type, @Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid);

    @PUT(base + "delete?type=image")
    Call<DeleteInfoRes> deleteProductImage(
            @Query("type") String type, @Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid, @Query("iid") int iid
    );
    @PUT(base + "delete?type=category")
    Call<DeleteInfoRes> deleteProductCategory(
            @Query("type") String type, @Query("uid") int uid, @Query("sid") int sid, @Query("pid") int pid, @Query("cid") int cid
    );

}
