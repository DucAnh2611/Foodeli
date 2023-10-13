package com.example.foodeli.Activities.ProductReview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.ProductDetail.ProductDetailReviewAdapter;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.GetProductReviewRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReview extends AppCompatActivity {

    private Pool pool;
    private HashMap<Integer, Integer> mapPage;
    private LinearLayout ratePickLayout, gridviewLayout;
    private GridView reviewGV;
    private ReviewListAdapter adapter;
    private int pid, currentRate;
    private HashMap<Integer, ArrayList<GetProductReviewRes.ReviewWithImage>> mapReview;
    private boolean showEmpty = false, canScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_review);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent pdetailIntent = getIntent();
        pid = pdetailIntent.getIntExtra("pid", 0);

        ratePickLayout = findViewById(R.id.product_review_rate_layout);
        gridviewLayout = findViewById(R.id.product_review_gridview_layout);
        reviewGV = findViewById(R.id.product_review_gridview);

        initHashMapPage();

        currentRate = 5;
        updateUiRate();

        for (int i = 0; i < ratePickLayout.getChildCount(); i++) {
            int finalI = i;
            ratePickLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI +1 != currentRate) {
                        currentRate = 5 - finalI;
                        updateUiRate();
                        canScroll = true;

                        loadListReviewByRate(pid, currentRate, mapPage.get(currentRate));
                    }
                }
            });
        }

        loadListReviewByRate(pid, 5, mapPage.get(currentRate));
        reviewGV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(canScroll && firstVisibleItem + visibleItemCount == totalItemCount) {
                    loadListReviewByRate(pid, currentRate, mapPage.get(currentRate));
                }
            }
        });
    }

    private void initHashMapPage() {

        mapReview = new HashMap<>();
        mapPage = new HashMap<>();

        for (int i = 1; i <=5 ; i++) {
            mapPage.put(i, 1);
            mapReview.put(i, new ArrayList<>());
        }

        adapter = new ReviewListAdapter(new ArrayList<>(), ProductReview.this);

    }
    private void loadListReviewByRate(int pid, int rate, int page) {
        pool = new Pool();

        Call<GetProductReviewRes> getReview = pool.getApiCallUserReview().getReviewOfProduct(pid, rate, page, 10) ;
        getReview.enqueue(new Callback<GetProductReviewRes>() {
            @Override
            public void onResponse(Call<GetProductReviewRes> call, Response<GetProductReviewRes> response) {
                if (!response.isSuccessful()) {
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
                    if(!response.body().getReviews().isEmpty()) {
                        canScroll = true;

                        mapPage.put(rate, mapPage.get(rate) +1);
                        ArrayList<GetProductReviewRes.ReviewWithImage> list, oldList;
                        list = response.body().getReviews();
                        oldList = mapReview.get(rate);

                        oldList.addAll(list);
                        mapReview.put(rate, oldList);

                        adapter.setList(oldList);

                        reviewGV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        reviewGV.setAdapter(adapter);
                    }
                    else {
                        canScroll = false;
                        if (mapReview.get(rate).size() == 0 && !showEmpty) {

                            gridviewLayout.setGravity(Gravity.CENTER);

                            TextView noItem = new TextView(ProductReview.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );

                            noItem.setText("No review found");
                            noItem.setTextColor(getColor(R.color.black));
                            noItem.setTextSize(17);
                            noItem.setGravity(Gravity.CENTER);

                            ImageView noItemImage = new ImageView(ProductReview.this);
                            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            noItemImage.setImageResource(R.drawable.no_data);

                            gridviewLayout.addView(noItem);
                            gridviewLayout.addView(noItemImage);
                            showEmpty = true;
                        }
                        else if(mapReview.get(rate).size() != 0) {

                            ArrayList<GetProductReviewRes.ReviewWithImage> oldList = mapReview.get(rate);
                            adapter.setList(oldList);

                            reviewGV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            reviewGV.setAdapter(adapter);
                        }
                        else {
                            reviewGV.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductReviewRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }
    private void updateUiRate() {
        for (int i = 0; i < ratePickLayout.getChildCount(); i++) {
            if( (5-i-1) == currentRate -1) ratePickLayout.getChildAt(i).setBackground(getDrawable(R.drawable.custom_input_style_stroke_green100));
            else ratePickLayout.getChildAt(i).setBackground(getDrawable(R.drawable.custom_input_style));
        }
    }

}