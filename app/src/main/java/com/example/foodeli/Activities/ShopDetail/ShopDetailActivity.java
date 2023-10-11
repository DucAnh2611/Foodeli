package com.example.foodeli.Activities.ShopDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetShopInformationResponse;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopDetailActivity extends AppCompatActivity {

    private Pool pool;
    private GetAllShopUserHaveResponse.ShopWithDetail shop;
    private ArrayList<GetTopProduct.ProductWithAvg> products;
    private int sid;

    private GridView listProductsGv;
    private ImageView shopImage;
    private TextView sName, sDesc, sRate, sSold, sProducts;
    private LinearLayout poolVoucherShop;
    private ItemProductShopAdapter adapter;
    private SupportImage supportImage = new SupportImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Gson gson = new Gson();
//        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        String json = mPrefs.getString("user", "");
//        User user = gson.fromJson(json, User.class);

        Intent productIntent = getIntent();
        sid = productIntent.getIntExtra("sid", 0);

        shopImage = findViewById(R.id.shop_detail_image);
        sName = findViewById(R.id.shop_detail_name);
        sDesc = findViewById(R.id.shop_detail_desc);
        sRate = findViewById(R.id.shop_detail_rate);
        sSold = findViewById(R.id.shop_detail_sold);
        sProducts = findViewById(R.id.shop_detail_product);
        poolVoucherShop = findViewById(R.id.shop_detail_pool_voucher);
        listProductsGv = findViewById(R.id.shop_detail_gridview_list_product);

        getShopProducts(sid);

        poolVoucherShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getShopProducts(int sid) {
        pool = new Pool();

        Call<GetShopInformationResponse> getList = pool.getApiCallUserShop().getShop(sid);
        getList.enqueue(new Callback<GetShopInformationResponse>() {
            @Override
            public void onResponse(Call<GetShopInformationResponse> call, Response<GetShopInformationResponse> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                        Toast.makeText(ShopDetailActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    shop = response.body().getShop().getInfo();
                    products = response.body().getShop().getProducts();

                    shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(shop.getAvatar()));
                    sName.setText(shop.getName());
                    sDesc.setText(shop.getDesc());
                    sRate.setText(String.valueOf(shop.getShopRating()));
                    sSold.setText(String.format("%d Sold", shop.getSold()));
                    sProducts.setText(String.format("%d Products", shop.getProductQuantity()));

                    adapter = new ItemProductShopAdapter(products, ShopDetailActivity.this);
                    listProductsGv.setAdapter(adapter);

                    int desiredHeight = (int) (products.size() * (getResources().getDimension(R.dimen.item_in_shop) + getResources().getDimension(R.dimen.item_gap_in_shop)) + listProductsGv.getPaddingTop() + listProductsGv.getPaddingBottom());
                    listProductsGv.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            desiredHeight
                    ));
                }
            }

            @Override
            public void onFailure(Call<GetShopInformationResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}