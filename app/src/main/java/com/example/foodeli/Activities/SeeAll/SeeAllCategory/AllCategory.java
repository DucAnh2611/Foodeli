package com.example.foodeli.Activities.SeeAll.SeeAllCategory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        GridView allCateGv = findViewById(R.id.cate_all_gv);
        ImageButton backBtn = findViewById(R.id.back_btn);
        ScrollView listLoading = findViewById(R.id.all_category_loading);

        allCateGv.setVisibility(View.GONE);
        listLoading.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Pool pool = new Pool();

        Call<CategoryRes> getCategories = pool.getApiCallGeneral().getSystemCategory(0);

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
                    GridViewCategoryAdapter adapter = new GridViewCategoryAdapter(response.body().getCategories(), AllCategory.this);
                    allCateGv.setAdapter(adapter);

                    allCateGv.setVisibility(View.VISIBLE);
                    listLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });

    }
}