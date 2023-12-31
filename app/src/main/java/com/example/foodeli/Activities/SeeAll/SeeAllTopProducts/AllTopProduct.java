package com.example.foodeli.Activities.SeeAll.SeeAllTopProducts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.foodeli.Activities.SeeAll.SeeAllCategory.AllCategory;
import com.example.foodeli.Activities.SeeAll.SeeAllCategory.GridViewCategoryAdapter;
import com.example.foodeli.Fragments.Home.TopProductGridViewAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.CategoryRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTopProduct extends AppCompatActivity {
    private ArrayList<GetTopProduct.ProductWithAvg> list = new ArrayList<>();
    private GridView allProGv = null;
    private ImageButton backBtn = null;
    private ScrollView listLoading;
    int page = 1;
    boolean canScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_top_product);

        allProGv = findViewById(R.id.pro_all_gv);
        backBtn = findViewById(R.id.back_btn);
        listLoading = findViewById(R.id.top_product_loading);

        allProGv.setVisibility(View.GONE);
        listLoading.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getMoreProduct(page);

    }

    private void getMoreProduct(int page) {
        final Pool pool = new Pool();

        Call<GetTopProduct> getTopProducts = pool.getApiCallGeneral().getTopProduct(page);

        getTopProducts.enqueue(new Callback<GetTopProduct>() {
            @Override
            public void onResponse(Call<GetTopProduct> call, Response<GetTopProduct> response) {
                if (response.code() != 200) {
                    canScroll= false;
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
                    list.addAll(response.body().getList());
                    if(response.body().getList().size() !=0) {
                        TopProductGridViewAdapter adapter = new TopProductGridViewAdapter(list, AllTopProduct.this);
                        allProGv.setAdapter(adapter);

                        allProGv.setVisibility(View.VISIBLE);
                        listLoading.setVisibility(View.GONE);
                    }
                    else{
                        canScroll= false;
                    }

                }
            }

            @Override
            public void onFailure(Call<GetTopProduct> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }
}