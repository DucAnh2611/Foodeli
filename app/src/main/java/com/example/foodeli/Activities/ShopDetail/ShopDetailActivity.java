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

import com.example.foodeli.Activities.PoolVoucherShop.PoolVoucherShop;
import com.example.foodeli.Activities.ProductDetail.DialogDeleteCart;
import com.example.foodeli.Activities.ProductDetail.ProductDetail;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Body.AddToCartBody;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.AddToCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.GetTopProduct;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllProductShop;
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
    private TextView sName, sDesc, sRate, sSold, sProducts, sAddress;
    private LinearLayout poolVoucherShop;
    private ItemProductShopAdapter adapter;
    private SupportImage supportImage = new SupportImage();
    private User user;

    private DialogDeleteCart dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent productIntent = getIntent();
        sid = productIntent.getIntExtra("sid", 0);

        shopImage = findViewById(R.id.shop_detail_image);
        sName = findViewById(R.id.shop_detail_name);
        sDesc = findViewById(R.id.shop_detail_desc);
        sAddress = findViewById(R.id.shop_detail_address);
        sRate = findViewById(R.id.shop_detail_rate);
        sSold = findViewById(R.id.shop_detail_sold);
        sProducts = findViewById(R.id.shop_detail_product);
        poolVoucherShop = findViewById(R.id.shop_detail_pool_voucher);
        listProductsGv = findViewById(R.id.shop_detail_gridview_list_product);

        getShopInformation(sid);
        getShopProducts(sid);

        poolVoucherShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VoucherPoolIntent = new Intent(ShopDetailActivity.this, PoolVoucherShop.class);
                VoucherPoolIntent.putExtra("sid", sid);
                startActivity(VoucherPoolIntent);
            }
        });
    }

    private void getShopInformation(int sid) {
        pool = new Pool();

        Call<GetShopInformationResponse> getInfoShop = pool.getApiCallUserShop().getShop(sid);
        getInfoShop.enqueue(new Callback<GetShopInformationResponse>() {
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
                    shop = response.body().getInfo();

                    shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(shop.getAvatar()));
                    sName.setText(shop.getName());
                    sDesc.setText(shop.getDesc());
                    sAddress.setText("Address: " + shop.getAddress());
                    sRate.setText(String.valueOf(shop.getShopRating()));
                    sSold.setText(String.format("%d Sold", shop.getSold()));
                    sProducts.setText(String.format("%d Products", shop.getProductQuantity()));
                }
            }

            @Override
            public void onFailure(Call<GetShopInformationResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void getShopProducts(int sid) {
        pool = new Pool();

        Call<GetAllProductShop> getList = pool.getApiCallUserShop().getShopProduct(sid);
        getList.enqueue(new Callback<GetAllProductShop>() {
            @Override
            public void onResponse(Call<GetAllProductShop> call, Response<GetAllProductShop> response) {
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
                    products = response.body().getProducts();

                    adapter = new ItemProductShopAdapter(products, ShopDetailActivity.this);
                    adapter.setOnAddToCart(new ItemProductShopAdapter.OnAddToCart() {
                        @Override
                        public void onAddToCart(int pid) {
                            addToCart(user.getId(), pid, 1);
                        }
                    });
                    listProductsGv.setAdapter(adapter);

                    int desiredHeight = (int) (products.size() * (getResources().getDimension(R.dimen.item_in_shop) + getResources().getDimension(R.dimen.item_gap_in_shop)) + listProductsGv.getPaddingTop() + listProductsGv.getPaddingBottom());
                    listProductsGv.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            desiredHeight
                    ));
                }
            }

            @Override
            public void onFailure(Call<GetAllProductShop> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void addToCart(int uid, int pid, int quantity) {
        pool = new Pool();

        AddToCartBody body = new AddToCartBody(uid, pid, quantity);
        Call<AddToCartRes> addToCartResCall = pool.getApiCallUserCart().addItemToCart(body);

        addToCartResCall.enqueue(new Callback<AddToCartRes>() {
            @Override
            public void onResponse(Call<AddToCartRes> call, Response<AddToCartRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        ShowDialogDeleteCart(pid);
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    Toast.makeText(ShopDetailActivity.this, response.body().isAdded() ? "Successfully" : "Failed on adding this item to cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void ShowDialogDeleteCart(int pid) {
        dialog = new DialogDeleteCart(ShopDetailActivity.this, pid, 1);
        dialog.show(getSupportFragmentManager(), "dialog_delete_cart");
    }
}