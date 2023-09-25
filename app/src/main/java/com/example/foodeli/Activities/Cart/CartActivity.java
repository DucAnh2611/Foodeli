package com.example.foodeli.Activities.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ArrayList<GetCartRes.ProductWithImage> list;
    private Pool pool = new Pool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageButton backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.cart_all_gv);
        LinearLayout main = findViewById(R.id.cart_main_holder);
        RelativeLayout addressSelect = findViewById(R.id.select_address_cart);
        RelativeLayout voucherSelect = findViewById(R.id.select_voucher_cart);
        RelativeLayout paymentSelect = findViewById(R.id.select_payment_cart);
        LinearLayout subTotal = findViewById(R.id.total_sum);
        Button placeOrder = findViewById(R.id.place_orer_btn);

        Call<GetCartRes> cartRes = pool.getApiCallUserCart().getCartUser(1);

        cartRes.enqueue(new Callback<GetCartRes>() {
            @Override
            public void onResponse(Call<GetCartRes> call, Response<GetCartRes> response) {
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
                    ArrayList<GetCartRes.ProductWithImage> res = response.body().getProducts();

                    list = new ArrayList<>();
                    list.addAll(res);

                    recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                    CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(list,CartActivity.this);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<GetCartRes> call, Throwable t) {
                System.out.println("false");
            }
        });

    }
}