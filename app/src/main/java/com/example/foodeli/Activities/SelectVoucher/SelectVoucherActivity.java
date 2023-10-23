package com.example.foodeli.Activities.SelectVoucher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Address.DialogFormAddress;
import com.example.foodeli.Activities.SelectAddress.SelectAddressActivity;
import com.example.foodeli.Activities.Voucher.DialogFindVoucher;
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
    private AppCompatButton findVoucher;
    private Button applyVoucher;
    private GridView gridviewVoucher;
    private LinearLayout gridviewVoucherHolder;
    private SelectVoucherAdapter adapter;
    private DialogFindVoucher dialogFindVoucher;
    private LinearLayout voucherEmpty;
    private ScrollView voucherLoading;

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
        findVoucher = findViewById(R.id.selectvoucher_find_btn);
        voucherEmpty = findViewById(R.id.select_voucher_empty);
        voucherLoading = findViewById(R.id.select_voucher_loading);
        gridviewVoucher = findViewById(R.id.selectvoucher_gridview);

        voucherEmpty.setVisibility(View.GONE);
        voucherLoading.setVisibility(View.VISIBLE);
        gridviewVoucher.setVisibility(View.GONE);

        adapter=new SelectVoucherAdapter(new ArrayList<>(), vid, vcode, SelectVoucherActivity.this);
        gridviewVoucher.setAdapter(adapter);

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
                    if(!vouchers.isEmpty()) {

                        adapter = new SelectVoucherAdapter(vouchers, vid, vcode, SelectVoucherActivity.this);
                        gridviewVoucher.setAdapter(adapter);

                        voucherEmpty.setVisibility(View.GONE);
                        voucherLoading.setVisibility(View.GONE);
                        gridviewVoucher.setVisibility(View.VISIBLE);

                    }
                    else {
                        voucherEmpty.setVisibility(View.VISIBLE);
                        voucherLoading.setVisibility(View.GONE);
                        gridviewVoucher.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<GetAllVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        findVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVoucherFind();
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
    private void openVoucherFind() {
        dialogFindVoucher = new DialogFindVoucher(SelectVoucherActivity.this, uid);
        dialogFindVoucher.setOnSuccessSaveVoucher(new DialogFindVoucher.OnSuccessSaveVoucher() {
            @Override
            public void onSuccessSaveVoucher(GetAllVoucherRes.VoucherWithRemain voucher) {
                adapter.AddToDataset(voucher);
                dialogFindVoucher.dismiss();

                voucherEmpty.setVisibility(View.GONE);
                voucherLoading.setVisibility(View.GONE);
                gridviewVoucher.setVisibility(View.VISIBLE);
            }
        });
        dialogFindVoucher.show(getSupportFragmentManager(), "find voucher_dialog_form" );
    }


}