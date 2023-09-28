package com.example.foodeli.Activities.SelectVoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectVoucherActivity extends AppCompatActivity {

    private Pool pool;
    private int uid, vid;
    private String vcode;
    private Intent cartIntent;

    private Button applyVoucher;
    private GridView gridviewVoucher;
    private LinearLayout gridviewVoucherHolder;
    private SelectVoucherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_voucher);

        pool = new Pool();

        cartIntent = getIntent();
        vid = cartIntent.getIntExtra("vid", 0);
        vcode = cartIntent.getStringExtra("vcode");
        uid = cartIntent.getIntExtra("uid", 0);

        ImageButton backBtn = findViewById(R.id.back_btn);
        applyVoucher = findViewById(R.id.selectvoucher_confirm_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtnClick(vid, vcode);
            }
        });

        pool = new Pool();

        Call<GetAllVoucherRes> allVoucherResCall = pool.getApiCallUserVoucher().getAllVoucher(uid);

        allVoucherResCall.enqueue(new Callback<GetAllVoucherRes>() {
            @Override
            public void onResponse(Call<GetAllVoucherRes> call, Response<GetAllVoucherRes> response) {
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
                    ArrayList<GetAllVoucherRes.VoucherWithRemain> vouchers = response.body().getVouchers();
                    gridviewVoucher = findViewById(R.id.selectvoucher_gridview);
                    if(!vouchers.isEmpty()) {

                        adapter = new SelectVoucherAdapter(vouchers, vid, vcode, SelectVoucherActivity.this);
                        gridviewVoucher.setAdapter(adapter);

                    }
                    else {
                        gridviewVoucherHolder = findViewById(R.id.selectvoucher_gridview_layout);
                        gridviewVoucherHolder.removeView(gridviewVoucher);

                        gridviewVoucherHolder.setGravity(Gravity.CENTER);

                        TextView noItem = new TextView(SelectVoucherActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        noItem.setText("No voucher found");
                        noItem.setTextColor(getColor(R.color.black));
                        noItem.setTextSize(17);
                        noItem.setGravity(Gravity.CENTER);

                        ImageView noItemImage = new ImageView(SelectVoucherActivity.this);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        noItemImage.setImageResource(R.drawable.no_data);

                        gridviewVoucherHolder.addView(noItem);
                        gridviewVoucherHolder.addView(noItemImage);
                    }

                }
            }

            @Override
            public void onFailure(Call<GetAllVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        applyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null) {
                    if(adapter.getTempVid() != 0 ){
                        backBtnClick(adapter.getTempVid(), adapter.getTempCode());
                    }
                    else Toast.makeText(SelectVoucherActivity.this, "No voucher select", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(SelectVoucherActivity.this, "No voucher select", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void backBtnClick(int vid, String vcode) {
        cartIntent.putExtra("vcode", vcode);
        cartIntent.putExtra("vid", vid);
        setResult(RESULT_OK, cartIntent);
        finish();
    }

}