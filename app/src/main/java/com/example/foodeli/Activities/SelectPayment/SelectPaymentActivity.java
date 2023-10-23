package com.example.foodeli.Activities.SelectPayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.foodeli.Activities.MethodCheckout.DialogPaymentForm;
import com.example.foodeli.Activities.MethodCheckout.OnSelectMethodUpdate;
import com.example.foodeli.Activities.MethodCheckout.PaymentMethodActivity;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.MethodSupportRes;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.GetAllMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Response.CreateMethodRes;
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
    private int ckid, uid, mid;
    private String cknum, cktype = "";
    private ArrayList<MethodSupport> methods;
    private HashMap<String, Integer> mapIconSupportList;
    private HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod;
    private GridView gridViewMethodSupport;
    private Button addMethodSupport, confirmMethod;
    private SelectPaymentAdapter adapter;
    private DialogPaymentForm dialogPaymentForm;
    private ConvertIconMethodIcon iconMethod = new ConvertIconMethodIcon();
    private ScrollView paymentLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        pool = new Pool();

        cartIntent = getIntent();
        ckid = cartIntent.getIntExtra("ckid", 0);
        cknum = cartIntent.getStringExtra("cknum");
        mid = cartIntent.getIntExtra("mid", 0);
        uid = cartIntent.getIntExtra("uid", 0);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtnClick(ckid, cknum, mid, iconMethod.convertTypeToIcon(cktype));
            }
        });

        gridViewMethodSupport = findViewById(R.id.selectpayment_gridview);
        addMethodSupport = findViewById(R.id.selectpayment_add_new_btn);
        confirmMethod = findViewById(R.id.selectpayment_confirm_btn);
        paymentLoading = findViewById(R.id.select_payment_loading);

        gridViewMethodSupport.setVisibility(View.GONE);
        paymentLoading.setVisibility(View.VISIBLE);

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
                    methods = response.body().getMethod();
                    mapIconSupportList = new HashMap<>();

                    for (MethodSupport method : methods) {
                        mapIconSupportList.put(method.getType(), iconMethod.convertTypeToIcon(method.getType()));
                        if(method.getMid() == mid) cktype = method.getType();
                    }
                    CallAPIUserHave();

                }
            }

            @Override
            public void onFailure(Call<MethodSupportRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        addMethodSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPaymentForm();
            }
        });
        confirmMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ckid = adapter.getCkSelect();
                cktype = adapter.getItemType();
                mid = adapter.getItemSelect();
                cknum = adapter.getItemNumber();
                backBtnClick(ckid, cknum, mid, iconMethod.convertTypeToIcon(cktype));
            }
        });

    }

    private void backBtnClick(int ckid, String cknum, int mid, int ckIcon) {
        cartIntent.putExtra("cknum", cknum);
        cartIntent.putExtra("ckid", ckid);
        cartIntent.putExtra("mid", mid);
        cartIntent.putExtra("ckicon", ckIcon);
        setResult(RESULT_OK, cartIntent);
        finish();
    }

    private void openPaymentForm() {
        dialogPaymentForm = new DialogPaymentForm(SelectPaymentActivity.this);
        dialogPaymentForm.setMapIconSupportList(mapIconSupportList);
        dialogPaymentForm.setMethods(methods);
        dialogPaymentForm.setOnSelectMethodUpdate(new OnSelectMethodUpdate() {
            @Override
            public void onCreatePayment(int mid, String type, String number, String expire) {
                submitPayment(mid, type, number, expire);
            }
        });

        dialogPaymentForm.show(getSupportFragmentManager(), "payment_dialog_form");
    }
    private void submitPayment(int mid, String type, String number, String expire) {
        CreateMethod body = new CreateMethod(uid, mid, number, expire, "");

        pool = new Pool();
        Call<CreateMethodRes> create = pool.getApiCallUserMethod().createMethod(body);

        create.enqueue(new Callback<CreateMethodRes>() {
            @Override
            public void onResponse(Call<CreateMethodRes> call, Response<CreateMethodRes> response) {
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
                    mapListMethod = adapter.getMapListMethod();
                    ArrayList<MethodWithTypeName> list = mapListMethod.get(type);
                    if(list == null) {
                        list = new ArrayList<>();
                    }
                    MethodWithTypeName methodWithTypeName = response.body().getMethod();
                    methodWithTypeName.setType(type);

                    list.add(methodWithTypeName);
                    mapListMethod.put(type, list);

                    adapter.setMapListMethod(mapListMethod);
                }
            }

            @Override
            public void onFailure(Call<CreateMethodRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void CallAPIUserHave() {
        pool = new Pool();

        Call<GetAllMethod> allMethodUserCall = pool.getApiCallUserMethod().getAllMethod(uid);
        allMethodUserCall.enqueue(new Callback<GetAllMethod>() {
            @Override
            public void onResponse(Call<GetAllMethod> call, Response<GetAllMethod> response) {
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
                    ArrayList<MethodWithTypeName> listMethodSupport = response.body().getList();
                    mapListMethod = new HashMap<>();

                    for (MethodWithTypeName item: listMethodSupport) {
                        ArrayList<MethodWithTypeName> addedEl = mapListMethod.containsKey(item.getType())
                                ? mapListMethod.get(item.getType())
                                : new ArrayList<>();

                        addedEl.add(item);
                        mapListMethod.put(item.getType(), addedEl);
                    }

                    adapter = new SelectPaymentAdapter(methods, uid, mid ,ckid, mapListMethod, cknum, mapIconSupportList, SelectPaymentActivity.this);
                    gridViewMethodSupport.setAdapter(adapter);

                    gridViewMethodSupport.setVisibility(View.VISIBLE);
                    paymentLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetAllMethod> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}