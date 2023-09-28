package com.example.foodeli.Activities.SelectPayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.MethodSupportRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPaymentActivity extends AppCompatActivity {
    private Pool pool;
    private Intent cartIntent;
    private int ckid, uid;
    private String cknum, cktype;
    private GridView gridViewMethodSupport;
    private Button addMethodSupport, confirmMethod;
    private SelectPaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        pool = new Pool();

        cartIntent = getIntent();
        ckid = cartIntent.getIntExtra("ckid", 0);
        cknum = cartIntent.getStringExtra("cknum");
        cktype = cartIntent.getStringExtra("cknum");
        uid = cartIntent.getIntExtra("uid", 0);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtnClick(ckid, cknum, cktype);
            }
        });

        gridViewMethodSupport = findViewById(R.id.selectpayment_gridview);
        addMethodSupport = findViewById(R.id.selectpayment_add_new_btn);
        confirmMethod = findViewById(R.id.selectpayment_confirm_btn);

        Call<MethodSupportRes> getMethodSupport = pool.getApiCallGeneral().getSystemMethod();

        getMethodSupport.enqueue(new Callback<MethodSupportRes>() {
            @Override
            public void onResponse(Call<MethodSupportRes> call, Response<MethodSupportRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(SelectPaymentActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<MethodSupport> methods = response.body().getMethod();
                    HashMap<String, Integer> mapIconSupportList = new HashMap<>();

                    for (MethodSupport method : methods) {
                        mapIconSupportList.put(method.getType(), convertTypeToIcon(method.getType()));
                    }

                    adapter = new SelectPaymentAdapter(methods, uid, ckid, cknum,
                            cktype, mapIconSupportList, SelectPaymentActivity.this);
                    gridViewMethodSupport.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<MethodSupportRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void backBtnClick(int ckid, String cknum, String cktype) {
        cartIntent.putExtra("cknum", cknum);
        cartIntent.putExtra("ckid", ckid);
        cartIntent.putExtra("cktype", cktype);
        setResult(RESULT_OK, cartIntent);
        finish();
    }

    private int convertTypeToIcon(String type){
        switch (type.toLowerCase()) {
            case "cash":
                return R.drawable.cash;
            case "visa":
                return R.drawable.visa;
            case "mastercard":
                return R.drawable.mastercard;
            case "paypal":
                return R.drawable.paypal;
            case "foodelipay":
                return R.drawable.logo;
            default:
                return 0;
        }
    }
}