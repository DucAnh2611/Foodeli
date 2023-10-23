package com.example.foodeli.Activities.Find;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Cart.CartActivity;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.Fragments.Home.TopProductGridViewAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindResultActivity extends AppCompatActivity {

    private Pool pool;
    private Filter filter;
    private int cid, page;
    private float min, max;
    private String key = "";
    private boolean loadMore = true;
    private AppCompatButton searchButton;
    private GridView gridView;
    private TopProductGridViewAdapter adapter;
    private LinearLayout gridviewHolder, findEmpty;
    private ScrollView findLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_result);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton = findViewById(R.id.find_res_key_btn);
        gridviewHolder = findViewById(R.id.find_res_gridview_holder);
        findEmpty = findViewById(R.id.find_gridview_empty);
        findLoading = findViewById(R.id.find_loading);

        gridView = findViewById(R.id.find_res_gridview_product);
        adapter = new TopProductGridViewAdapter(new ArrayList<>(), this);
        gridView.setAdapter(adapter);

        findEmpty.setVisibility(View.GONE);
        findLoading.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);

        Intent FindIntent = getIntent();
        filter = new Filter();

        key = FindIntent.getStringExtra("key");
        min = FindIntent.getFloatExtra("min", 0);
        max = FindIntent.getFloatExtra("max", 0);
        cid = FindIntent.getIntExtra("cid", 0);
        page = FindIntent.getIntExtra("page", 1);

        filter.setKey(key == null ? "" : key);
        filter.setMin(min);
        filter.setMax(max);
        filter.setCid(cid);
        filter.setPage(page);

        if (key == null || key.equals("")) {
            searchButton.setHint("Search...");
        }
        else {
            searchButton.setText(key);
        }

        searchByFilter(filter);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // Check if the last visible position is equal to the total item count - 1
                    if (view.getLastVisiblePosition() == gridView.getAdapter().getCount() - 1 && loadMore) {
                        page++;

                        filter.setPage(page);
                        searchByFilter(filter);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FindIntent = new Intent(FindResultActivity.this, FindActivity.class);
                FindIntent.putExtra("key", key);
                startActivity(FindIntent);
                finish();
            }
        });
    }

    private void searchByFilter(Filter filter) {
        pool = new Pool();

        Call<GetTopProduct> findProduct = pool.getApiCallShopProduct().findProduct(
                filter.getKey(), filter.getMin(), filter.getMax(), filter.getCid(), filter.getPage()
        );

        findProduct.enqueue(new Callback<GetTopProduct>() {
            @Override
            public void onResponse(Call<GetTopProduct> call, Response<GetTopProduct> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(FindResultActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<GetTopProduct.ProductWithAvg> listRes = response.body().getList();
                    if(!listRes.isEmpty()) {
                        loadMore = true;
                        adapter.addItems(listRes);
                        adapter.notifyDataSetChanged();

                        findEmpty.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                    }
                    else {
                        loadMore = false;

                        findEmpty.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                    }

                    findLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetTopProduct> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}