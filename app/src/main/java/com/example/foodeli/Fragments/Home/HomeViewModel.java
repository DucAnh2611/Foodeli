package com.example.foodeli.Fragments.Home;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.GetUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private Pool pool = new Pool();
    private MutableLiveData<ArrayList<Category>> categories;
    private MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>> topProducts;

    public LiveData<ArrayList<Category>> getCategories() {
        if (this.categories == null) {
            this.categories = new MutableLiveData<ArrayList<Category>>();
            loadCategories();
        }
        return this.categories;
    }

    public LiveData<ArrayList<GetTopProduct.ProductWithAvg>> getTopProducts() {
        if (this.topProducts == null) {
            this.topProducts = new MutableLiveData<ArrayList<GetTopProduct.ProductWithAvg>>();
            loadTopProduct();
        }
        return this.topProducts;
    }

    private void loadCategories() {
        Call<CategoryRes> getCategories = pool.getApiCallGeneral().getSystemCategory(6);

        getCategories.enqueue(new Callback<CategoryRes>() {
            @Override
            public void onResponse(Call<CategoryRes> call, Response<CategoryRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    categories.setValue(response.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void loadTopProduct() {
        Call<GetTopProduct> getTopProduct = pool.getApiCallGeneral().getTopProduct(1);

        getTopProduct.enqueue(new Callback<GetTopProduct>() {
            @Override
            public void onResponse(Call<GetTopProduct> call, Response<GetTopProduct> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    topProducts.setValue(response.body().getList());
                }
            }

            @Override
            public void onFailure(Call<GetTopProduct> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

}
