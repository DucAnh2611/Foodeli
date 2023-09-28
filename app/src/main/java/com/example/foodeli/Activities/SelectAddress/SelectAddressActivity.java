package com.example.foodeli.Activities.SelectAddress;

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
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.GetAllAddressRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAddressActivity extends AppCompatActivity {
    private int aid, uid;
    private String aname;
    private Intent cartIntent;
    private GridView gridview;
    private LinearLayout gridviewLayout;
    private SelectAddressAdapter adapter;
    private Button applyAddBtn;
    private Pool pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        pool = new Pool();

        cartIntent = getIntent();
        aid = cartIntent.getIntExtra("aid", 0);
        aname = cartIntent.getStringExtra("aname");
        uid = cartIntent.getIntExtra("uid", 0);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtnClick(aid, aname);
            }
        });

        gridview = findViewById(R.id.selectaddress_gridview);
        applyAddBtn = findViewById(R.id.selectaddress_confirm_btn);

        Call<GetAllAddressRes> getAllAddress = pool.getApiCallUserAddress().getAll(uid);

        getAllAddress.enqueue(new Callback<GetAllAddressRes>() {
            @Override
            public void onResponse(Call<GetAllAddressRes> call, Response<GetAllAddressRes> response) {
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
                    ArrayList<Address> listAdd = response.body().getList();
                    gridviewLayout = findViewById(R.id.selectaddress_gridview_layout);
                    if(!listAdd.isEmpty()) {
                        adapter = new SelectAddressAdapter(listAdd, aid, aname, SelectAddressActivity.this);
                        gridview.setAdapter(adapter);
                    }
                    else {
                        gridviewLayout.removeView(gridview);

                        gridviewLayout.setGravity(Gravity.CENTER);

                        TextView noItem = new TextView(SelectAddressActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        noItem.setText("No address found");
                        noItem.setTextColor(getColor(R.color.black));
                        noItem.setTextSize(17);
                        noItem.setGravity(Gravity.CENTER);

                        ImageView noItemImage = new ImageView(SelectAddressActivity.this);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        noItemImage.setImageResource(R.drawable.no_data);

                        gridviewLayout.addView(noItem);
                        gridviewLayout.addView(noItemImage);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        applyAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getTempSelect() != 0) {
                    backBtnClick(adapter.getTempSelect(), adapter.getTempName());
                }
                else Toast.makeText(SelectAddressActivity.this, "No address selected", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void backBtnClick(int aid, String aname) {
        cartIntent.putExtra("aName", aname);
        cartIntent.putExtra("aid", aid);
        setResult(RESULT_OK, cartIntent);
        finish();
    }
}