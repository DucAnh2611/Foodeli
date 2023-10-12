package com.example.foodeli.Activities.PoolVoucherShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Response.GetAllVoucherShopRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoolVoucherShop extends AppCompatActivity {

    private Pool pool;
    private GridView poolVoucher;
    private LinearLayout poolVoucherLayout;
    private int sid;
    private PoolVoucherGridviewAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_voucher_shop);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent ShopIntent = getIntent();
        sid = ShopIntent.getIntExtra("sid", sid);

        poolVoucher = findViewById(R.id.pool_voucher_gridview);
        poolVoucherLayout = findViewById(R.id.pool_voucher_gridview_layout);

        getPoolVoucherShop(sid);

    }

    private void getPoolVoucherShop(int sid) {
        pool = new Pool();

        Call<GetAllVoucherShopRes> getAll = pool.getApiCallShopVoucher().getAlVoucherShop(sid);
        getAll.enqueue(new Callback<GetAllVoucherShopRes>() {
            @Override
            public void onResponse(Call<GetAllVoucherShopRes> call, Response<GetAllVoucherShopRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(PoolVoucherShop.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    if(!response.body().getItems().isEmpty()) {
                        adapter = new PoolVoucherGridviewAdapter(response.body().getItems(), PoolVoucherShop.this);
                        poolVoucher.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        poolVoucher.setAdapter(adapter);
                    }
                    else {
                        poolVoucher.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                        poolVoucherLayout.setGravity(Gravity.CENTER);

                        TextView noItem = new TextView(PoolVoucherShop.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        noItem.setText("No voucher found");
                        noItem.setTextColor(getColor(R.color.black));
                        noItem.setTextSize(17);
                        noItem.setGravity(Gravity.CENTER);

                        ImageView noItemImage = new ImageView(PoolVoucherShop.this);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        noItemImage.setImageResource(R.drawable.no_data);

                        poolVoucherLayout.addView(noItem);
                        poolVoucherLayout.addView(noItemImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllVoucherShopRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}