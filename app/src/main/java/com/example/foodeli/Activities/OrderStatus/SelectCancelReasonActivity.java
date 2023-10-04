package com.example.foodeli.Activities.OrderStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.CancelReason;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body.CancelOrderBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.CancelRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCancelReasonActivity extends AppCompatActivity implements CancelReasonAdapter.OnClickReason {

    private HomeViewModel homeViewModel;
    private GridView selectReasonGV;
    private EditText otherReason;
    private LinearLayout otherLayout;
    private CancelReasonAdapter adapter;
    private Button confirmCancel;
    private int rid = 0;
    private int oid;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cancel_reason);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);
        
        Intent orderIntent = getIntent();
        oid = orderIntent.getIntExtra("oid", 0);

        selectReasonGV = findViewById(R.id.cancel_reason_gridview);
        otherLayout = findViewById(R.id.cancel_reason_other_layout);
        otherLayout.setVisibility(View.INVISIBLE);
        otherReason = findViewById(R.id.cancel_reason_other_context);
        confirmCancel = findViewById(R.id.cancel_order_btn);

        homeViewModel.getListReason().observe(this, new Observer<ArrayList<CancelReason>>() {
            @Override
            public void onChanged(ArrayList<CancelReason> reasons) {
                adapter = new CancelReasonAdapter(reasons, rid, SelectCancelReasonActivity.this);
                adapter.setOnItemClickReason(SelectCancelReasonActivity.this);
                selectReasonGV.setAdapter(adapter);
            }
        });

        confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rid !=0) {
                    CancelOrderBody body = new CancelOrderBody(oid, user.getId(), rid, desc);
                    Pool pool = new Pool();
                    Call<CancelRes> cancelResCall = pool.getApiCallUserOrder().cancelOrder(body);

                    cancelResCall.enqueue(new Callback<CancelRes>() {
                        @Override
                        public void onResponse(Call<CancelRes> call, Response<CancelRes> response) {
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
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelRes> call, Throwable t) {
                            System.out.print(t.getMessage());
                        }
                    });

                }
            }
        });

        otherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                desc = s.toString();
            }
        });

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClickReason() {
        rid = adapter.getCurrId();
        if(!otherReason.getText().toString().equals("")) {
            otherReason.setText("");
        }
        if(rid != 6) {
            otherLayout.setVisibility(View.INVISIBLE);
        }
        else {
            otherLayout.setVisibility(View.VISIBLE);
        }
    }
}