package com.example.foodeli.Activities.Voucher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherAdapter;
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

public class Voucher extends AppCompatActivity {

    private GridView voucherGV;
    private Pool pool;
    private AppCompatButton findVoucher;
    private ArrayList<GetAllVoucherRes.VoucherWithRemain> listVou;
    private LinearLayout gridviewVoucherHolder;
    private UserVoucherGridviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent CartIntent = getIntent();
        int uid = CartIntent.getIntExtra("uid", 0);

        voucherGV = findViewById(R.id.user_voucher_gridview);
        findVoucher = findViewById(R.id.user_voucher_find_btn);

        getVoucherList(uid);
        findVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFindVoucher(uid);
            }
        });
    }

    private void getVoucherList(int uid) {
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
                    listVou = response.body().getVouchers();
                    if(!listVou.isEmpty()) {
                        adapter = new UserVoucherGridviewAdapter(listVou, Voucher.this);
                        voucherGV.setAdapter(adapter);
                    }
                    else {
                        gridviewVoucherHolder = findViewById(R.id.user_voucher_gridview_layout);
                        gridviewVoucherHolder.removeView(voucherGV);

                        gridviewVoucherHolder.setGravity(Gravity.CENTER);

                        TextView noItem = new TextView(Voucher.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        noItem.setText("No voucher found");
                        noItem.setTextColor(getColor(R.color.black));
                        noItem.setTextSize(17);
                        noItem.setGravity(Gravity.CENTER);

                        ImageView noItemImage = new ImageView(Voucher.this);
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
    }

    private void openDialogFindVoucher(int uid) {
        DialogFindVoucher dialog = new DialogFindVoucher(Voucher.this, uid);
        dialog.setOnSuccessSaveVoucher(new DialogFindVoucher.OnSuccessSaveVoucher() {
            @Override
            public void onSuccessSaveVoucher(GetAllVoucherRes.VoucherWithRemain voucher) {
                listVou.add(voucher);
                adapter.setListVou(listVou);
            }
        });
        dialog.show(getSupportFragmentManager(), "dialog_find_voucher");
    }
}