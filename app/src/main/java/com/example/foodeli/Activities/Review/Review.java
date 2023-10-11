package com.example.foodeli.Activities.Review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.foodeli.Activities.OrderDetail.OrderDetailActivity;
import com.example.foodeli.Activities.OrderDetail.OrderDetailProductsAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Body.CreateReviewBody;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.CreateReviewRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderItemsRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Review extends AppCompatActivity {
    private Pool pool;
    private int rateSelected = -1, oid, uid;
    private String desc;
    private RecyclerView productList;
    private ShapeableImageView productImage;
    private LinearLayout rateLayout;
    private EditText description;
    private AppCompatButton submit;
    private ArrayList<OrderItemsRes.OrderItems> orderItems;
    private ProductRecyclerViewAdapter adapter;
    private OrderItemsRes.OrderItems selectedItem;
    private SupportImage supportImage = new SupportImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        uid = user.getId();

        Intent orderIntent = getIntent();
        oid = orderIntent.getIntExtra("oid", 0);

        productList = findViewById(R.id.review_recyclerview_products);
        productImage = findViewById(R.id.review_product_form_image);
        rateLayout = findViewById(R.id.review_product_rate);
        description = findViewById(R.id.review_description);
        submit = findViewById(R.id.review_submit);
        orderItems = new ArrayList<>();

        description.addTextChangedListener(new TextWatcher() {
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
        productList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getOrderItems(oid, uid);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeReviewItem();
            }
        });
    }

    private void getOrderItems(int oid, int uid) {
        pool = new Pool();

        Call<OrderItemsRes> getOrderItems = pool.getApiCallUserOrder().getLIstItemsInOrder(oid, uid);
        getOrderItems.enqueue(new Callback<OrderItemsRes>() {
            @Override
            public void onResponse(Call<OrderItemsRes> call, Response<OrderItemsRes> response) {
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
                    ArrayList<OrderItemsRes.OrderItems> res = response.body().getItems();
                    for (OrderItemsRes.OrderItems item: res) {
                        if(item.getCanReview() ==1) {
                            orderItems.add(item);
                        }
                    }

                    if(orderItems.isEmpty()) {
                        finish();
                        Toast.makeText(Review.this, "Nothing to review", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        adapter = new ProductRecyclerViewAdapter(orderItems, Review.this);
                        defaultStateAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderItemsRes> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
    }

    private void placeReviewItem() {
        pool = new Pool();

        CreateReviewBody body = new CreateReviewBody(uid, selectedItem.getPid(), oid, rateSelected+1, "", desc, new ArrayList<>());

        Call<CreateReviewRes> getOrderItems = pool.getApiCallUserReview().createReview(body);
        getOrderItems.enqueue(new Callback<CreateReviewRes>() {
            @Override
            public void onResponse(Call<CreateReviewRes> call, Response<CreateReviewRes> response) {
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
                    orderItems.remove(orderItems.indexOf(selectedItem));
                    adapter.setList(orderItems);
                    adapter.notifyDataSetChanged();
                    if(orderItems.isEmpty()) {
                        finish();
                        Toast.makeText(Review.this, "Nothing to review", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateReviewRes> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
    }

    private void defaultStateAdapter(ProductRecyclerViewAdapter adapter) {
        selectedItem = orderItems.get(0);
        updateUiToItem(selectedItem);
        adapter.setOnSelectProductReview(new ProductRecyclerViewAdapter.OnSelectProductReview() {
            @Override
            public void onSelectProductReview() {
                if(selectedItem.getPid() != adapter.getSelectItem().getPid() ) {
                    selectedItem = adapter.getSelectItem();
                    updateUiToItem(selectedItem);
                }

            }
        });
        productList.setAdapter(adapter);

    }

    private void updateUiToItem(OrderItemsRes.OrderItems item) {
        productImage.setImageBitmap(supportImage.convertBase64ToBitmap(item.getImage()));
        rateSelected = -1;
        desc= "";
        description.setText(desc);
        setRateStar(rateSelected);
    }

    private void setRateStar(int rate) {

        for (int i = 0; i < rateLayout.getChildCount(); i++) {
            ImageButton starAtIndex = (ImageButton) rateLayout.getChildAt(i);
            int color = getColor(R.color.grey_300);
            if(i <= rate) color = getColor(R.color.orange_400);

            starAtIndex.setColorFilter(color);
            int tempI = i;
            starAtIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rateSelected == tempI) rateSelected = -1;
                    else rateSelected = tempI;
                    setRateStar(rateSelected);
                }
            });
        }

    }

}