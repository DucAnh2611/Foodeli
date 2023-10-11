package com.example.foodeli.Activities.Address;

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
import android.widget.Toast;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Body.CreateAddress;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.CreateAddressRes;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.DeleteAddressRes;
import com.example.foodeli.MySqlSetUp.Schemas.Address.Response.GetAllAddressRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Address extends AppCompatActivity {

    private Pool pool;
    private int uid;
    private AppCompatButton newAddress;
    private GridView listAddress;
    private LinearLayout gridviewLayout;
    private UserAddressGridViewAdapter adapter;
    private DialogFormAddress dialogForm;
    private ArrayList<com.example.foodeli.MySqlSetUp.Schemas.Address.Address> listAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent profileIntent = getIntent();
        uid = profileIntent.getIntExtra("uid", 0);

        newAddress = findViewById(R.id.user_address_add_new_btn);
        listAddress = findViewById(R.id.user_address_gridview);

        getAddressFromUser(uid);

        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressForm();
            }
        });
    }
    private void getAddressFromUser(int uid){
        pool = new Pool();

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
                    gridviewLayout = findViewById(R.id.user_address_gridview_layout);
                    if(!listAdd.isEmpty()) {
                        adapter = new UserAddressGridViewAdapter(listAdd, Address.this);
                        adapter.setOnSelectMethodAddress(new OnSelectMethodAddress() {
                            @Override
                            public void onSelectUpdate(int position) {
                                openAddressForm(position);
                            }

                            @Override
                            public void onSelectDelete(int position) {
                                deleteAddress(position);
                            }
                        });
                        listAddress.setAdapter(adapter);
                    }
                    else {
                        gridviewLayout.removeView(listAddress);

                        gridviewLayout.setGravity(Gravity.CENTER);

                        TextView noItem = new TextView(Address.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        noItem.setText("No address found");
                        noItem.setTextColor(getColor(R.color.black));
                        noItem.setTextSize(17);
                        noItem.setGravity(Gravity.CENTER);

                        ImageView noItemImage = new ImageView(Address.this);
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
    }
    private void openAddressForm(int position) {
        dialogForm = new DialogFormAddress(Address.this, uid, listAdd.get(position));
        dialogForm.setHandleFormAddress(new DialogFormAddress.HandleFormAddress() {
            @Override
            public void onUpdateAddress(com.example.foodeli.MySqlSetUp.Schemas.Address.Address address, String newAdd) {
                updateAddress(uid, address.getAid(), newAdd, position);
            }

            @Override
            public void onCreateAddress(String address) {}
        });
        dialogForm.show(getSupportFragmentManager(), "address_dialog_form" );
    }
    private void openAddressForm() {
        dialogForm = new DialogFormAddress(Address.this, uid);
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
    private void deleteAddress(int position) {
        pool = new Pool();

        Call<DeleteAddressRes> deleteAddress = pool.getApiCallUserAddress().deleteAddress(uid, listAdd.get(position).getAid());

        deleteAddress.enqueue(new Callback<DeleteAddressRes>() {
            @Override
            public void onResponse(Call<DeleteAddressRes> call, Response<DeleteAddressRes> response) {
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
                    listAdd.remove(position);
                    adapter.setListAdd(listAdd);
                }
            }

            @Override
            public void onFailure(Call<DeleteAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void updateAddress(int uid, int aid, String newAdd, int position) {
        com.example.foodeli.MySqlSetUp.Schemas.Address.Address body =
                new com.example.foodeli.MySqlSetUp.Schemas.Address.Address(aid, uid, newAdd, 0);
        pool = new Pool();

        Call<CreateAddressRes> updateAdd = pool.getApiCallUserAddress().updateAddress(body);
        updateAdd.enqueue(new Callback<CreateAddressRes>() {
            @Override
            public void onResponse(Call<CreateAddressRes> call, Response<CreateAddressRes> response) {
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
                    listAdd.get(position).setAddress(newAdd);
                    adapter.setListAdd(listAdd);
                }
            }

            @Override
            public void onFailure(Call<CreateAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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
                        Toast.makeText(Address.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    listAdd.add(response.body().getAddress());
                    adapter.setListAdd(listAdd);
                }
            }

            @Override
            public void onFailure(Call<CreateAddressRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}