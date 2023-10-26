package com.example.foodeli.Activities.SelectAddress;

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
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Body.CreateAddress;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.CreateAddressRes;
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
    private int aid, uid, t_aid;
    private ArrayList<Address> listAdd;
    private String aname, t_aname;
    private Intent cartIntent;
    private GridView gridview;
    private LinearLayout gridviewLayout;
    private SelectAddressAdapter adapter;
    private AppCompatButton addNewAddress;
    private LinearLayout addressEmpty;
    private ScrollView addressLoading;
    private Button applyAddBtn;
    private Pool pool;
    private DialogFormAddress dialogForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        pool = new Pool();

        cartIntent = getIntent();
        t_aid = aid = cartIntent.getIntExtra("aid", 0);
        t_aname = aname = cartIntent.getStringExtra("aname");
        uid = cartIntent.getIntExtra("uid", 0);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtnClick(aid, aname);
            }
        });

        addNewAddress = findViewById(R.id.selectaddress_add_new_btn);
        gridview = findViewById(R.id.selectaddress_gridview);
        applyAddBtn = findViewById(R.id.selectaddress_confirm_btn);
        addressEmpty = findViewById(R.id.select_address_empty);
        addressLoading = findViewById(R.id.select_address_loading);

        gridview.setVisibility(View.GONE);
        addressEmpty.setVisibility(View.GONE);
        addressLoading.setVisibility(View.VISIBLE);

        adapter = new SelectAddressAdapter(new ArrayList<>(), aid, aname, SelectAddressActivity.this);
        adapter.setOnSelectAddress(new SelectAddressAdapter.OnSelectAddress() {
            @Override
            public void onSelectAddress(int position) {
                if(t_aid != listAdd.get(position).getAid()) {
                    t_aid = listAdd.get(position).getAid();
                    t_aname = listAdd.get(position).getAddress();
                }
                else {
                    t_aid = 0;
                    t_aname = "Select Address";
                }
            }
        });
        gridview.setAdapter(adapter);

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
                    listAdd = response.body().getList();
                    if(!listAdd.isEmpty()) {
                        adapter.setListAdd(listAdd);

                        gridview.setVisibility(View.VISIBLE);
                        addressEmpty.setVisibility(View.GONE);
                        addressLoading.setVisibility(View.GONE);
                    }
                    else {
                        gridview.setVisibility(View.GONE);
                        addressEmpty.setVisibility(View.VISIBLE);
                        addressLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressForm();
            }
        });
        applyAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t_aid != 0) {
                    aid = t_aid;
                    aname = t_aname;
                    backBtnClick(aid, aname);
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

    private void openAddressForm() {
        dialogForm = new DialogFormAddress(SelectAddressActivity.this, uid);
        dialogForm.setHandleFormAddress(new DialogFormAddress.HandleFormAddress() {
            @Override
            public void onUpdateAddress(com.example.foodeli.MySqlSetUp.Schemas.Address.Address address, String newAdd) {
            }

            @Override
            public void onCreateAddress(String address) {
                createAddress(uid, address);
            }
        });
        dialogForm.show(getSupportFragmentManager(), "address_dialog_form" );
    }

    private void createAddress(int uid, String newAdd) {
        CreateAddress body = new CreateAddress(newAdd, uid);
        pool = new Pool();

        Call<CreateAddressRes> createAdd = pool.getApiCallUserAddress().createAddress(body);
        createAdd.enqueue(new Callback<CreateAddressRes>() {
            @Override
            public void onResponse(Call<CreateAddressRes> call, Response<CreateAddressRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(SelectAddressActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {

                    listAdd.add(response.body().getAddress());
                    adapter.setListAdd(listAdd);

                    adapter.setOnSelectAddress(new SelectAddressAdapter.OnSelectAddress() {
                        @Override
                        public void onSelectAddress(int position) {
                            if(t_aid != listAdd.get(position).getAid()) {
                                t_aid = listAdd.get(position).getAid();
                                t_aname = listAdd.get(position).getAddress();
                            }
                            else {
                                t_aid = 0;
                                t_aname = "Select Address";
                            }
                        }
                    });

                    gridview.setVisibility(View.VISIBLE);
                    addressEmpty.setVisibility(View.GONE);
                    addressLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CreateAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}