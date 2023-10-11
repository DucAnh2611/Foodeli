package com.example.foodeli.Activities.MethodCheckout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.SelectPayment.ConvertIconMethodIcon;
import com.example.foodeli.Activities.SelectPayment.SelectPaymentActivity;
import com.example.foodeli.Activities.SelectPayment.SelectPaymentAdapter;
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

public class PaymentMethodActivity extends AppCompatActivity {

    private Pool pool;
    private ConvertIconMethodIcon iconMethod;
    private SystemPaymentAdapter adapter;
    private GridView gridViewMethodSupport;
    private AppCompatButton addNewBtn;
    private HashMap<String, Integer> mapIconSupportList;
    private HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod = new HashMap<>();
    private ArrayList<MethodSupport> methods;
    private DialogPaymentForm dialog;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        pool = new Pool();
        iconMethod = new ConvertIconMethodIcon();

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent CartIntent = getIntent();
        uid = CartIntent.getIntExtra("uid", 0);

        addNewBtn = findViewById(R.id.user_method_add_new_btn);
        gridViewMethodSupport = findViewById(R.id.user_method_gridview);

        Call<MethodSupportRes> getMethodSupport = pool.getApiCallGeneral().getSystemMethod();

        getMethodSupport.enqueue(new Callback<MethodSupportRes>() {
            @Override
            public void onResponse(Call<MethodSupportRes> call, Response<MethodSupportRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(PaymentMethodActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    methods = response.body().getMethod();
                    mapIconSupportList = new HashMap<>();

                    for (MethodSupport method : methods) {
                        mapIconSupportList.put(method.getType(), iconMethod.convertTypeToIcon(method.getType()));
                    }
                    CallAPIUserHave();

                }
            }

            @Override
            public void onFailure(Call<MethodSupportRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogUpdate();
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
                        Toast.makeText(PaymentMethodActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<MethodWithTypeName> listMethodSupport = response.body().getList();

                    for (MethodWithTypeName item: listMethodSupport) {
                        ArrayList<MethodWithTypeName> addedEl = mapListMethod.containsKey(item.getType())
                                ? mapListMethod.get(item.getType())
                                : new ArrayList<>();

                        addedEl.add(item);
                        mapListMethod.put(item.getType(), addedEl);
                    }
                    adapter = new SystemPaymentAdapter(methods, mapIconSupportList, mapListMethod,PaymentMethodActivity.this);
                    gridViewMethodSupport.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetAllMethod> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void openDialogUpdate() {
        dialog = new DialogPaymentForm(PaymentMethodActivity.this);

        dialog.setMapIconSupportList(mapIconSupportList);
        dialog.setMethods(methods);
        dialog.setOnSelectMethodUpdate(new OnSelectMethodUpdate() {
            @Override
            public void onCreatePayment(int mid, String type, String number, String expire) {
                submitPayment(mid, type, number, expire);
            }
        });

        dialog.show(getSupportFragmentManager(), "dialog_form_payment");
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
                        Toast.makeText(PaymentMethodActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<MethodWithTypeName> list = mapListMethod.get(type);
                    if(list == null) {list = new ArrayList<>();}
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

}