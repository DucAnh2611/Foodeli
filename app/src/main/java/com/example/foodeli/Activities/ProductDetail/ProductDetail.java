package com.example.foodeli.Activities.ProductDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.ProductReview.ProductReview;
import com.example.foodeli.Activities.ProductReview.ReviewListAdapter;
import com.example.foodeli.Activities.ShopDetail.ShopDetailActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Body.AddToCartBody;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.AddToCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Response.GetProductReviewRes;
import com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Response.GetProductInfo;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {

    private GetProductInfo.ProductInfo product = null;
    private Pool pool = null;
    private LinearLayout dotsLayout = null, reviewLayout;
    private List<View> dotViews = new ArrayList<>();
    private int quantity = 1;
    private int pid;
    private ListView reviewListView;
    private HomeViewModel homeViewModel;
    private ReviewListAdapter adapter;
    private AppCompatButton seeAllBtn;
    private TextView nothing;
    private SupportImage supportImage = new SupportImage();
    private DialogDeleteCart dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        ImageButton backBtn = findViewById(R.id.back_btn);
        LinearLayout rateHolder = findViewById(R.id.productdetail_rate_holder);
        reviewLayout = findViewById(R.id.productdetail_review_layout);
        reviewListView = findViewById(R.id.productdetail_review_listview);
        seeAllBtn = findViewById(R.id.productdetail_review_seeall);
        nothing = findViewById(R.id.productdetail_review_empty_text);

        seeAllBtn.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        Intent productIntent = getIntent();
        pid = productIntent.getIntExtra("pid", 0);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productReview = new Intent(ProductDetail.this, ProductReview.class);
                productReview.putExtra("pid", pid);
                startActivity(productReview);
            }
        });

        getProductInfomation(pid);
        loadListReViewByRate(pid, -1, 1);

    }

    private void getProductInfomation(int pid) {
        pool = new Pool();

        Call<GetProductInfo> getProduct = pool.getApiCallShopProduct().getProductInfo(pid);

        getProduct.enqueue(new Callback<GetProductInfo>() {
            @Override
            public void onResponse(Call<GetProductInfo> call, Response<GetProductInfo> response) {
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
                    product = response.body().getItem();

                    ViewPager imageViewPager = findViewById(R.id.productdetail_viewpager_image);
                    ArrayList<GetProductInfo.ImageWithId> imageList = product.getImages();
                    ProductDetailImageAdapter imageAdapter = new ProductDetailImageAdapter(imageList, ProductDetail.this);
                    imageViewPager.setAdapter(imageAdapter);

                    dotsLayout = findViewById(R.id.productdetail_dots_index_image);
                    InitDots dots = new InitDots(ProductDetail.this, dotsLayout);
                    dots.createDotIndicators(imageList.size());
                    imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            dots.updateDotIndicator(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    EditText quan = findViewById(R.id.productdetail_p_quantity);
                    ImageButton minus = findViewById(R.id.productdetail_p_minus);
                    ImageButton add = findViewById(R.id.productdetail_p_add);

                    TextView name = findViewById(R.id.productdetail_name);
                    TextView shortDesc = findViewById(R.id.productdetail_shortdesc);
                    TextView longDesc = findViewById(R.id.productdetail_longdesc);
                    TextView rate = findViewById(R.id.productdetail_rate);
                    TextView estimate = findViewById(R.id.productdetail_estimate);
                    TextView onStock = findViewById(R.id.productdetail_stock);
                    TextView price = findViewById(R.id.productdetail_price);
                    LinearLayout shopLayout = findViewById(R.id.productdetail_shop_layout);
                    TextView shopName = findViewById(R.id.productdetail_shop_name);
                    ShapeableImageView shopImage = findViewById(R.id.productdetail_shop_image);

                    DecimalFormat df = new DecimalFormat("#.#");

                    quan.setText(String.valueOf(quantity));
                    name.setText(product.getProduct().getName());
                    shortDesc.setText(product.getProduct().getShortDesc());
                    longDesc.setText(product.getProduct().getLongDesc());
                    onStock.setText( String.valueOf(product.getProduct().getStock()) );
                    price.setText(
                            df.format(product.getProduct().getPrice()) + "/" +
                            String.valueOf(product.getProduct().getUnit())
                    );

                    rate.setText(df.format(product.getProduct().getRate()));
                    estimate.setText( String.valueOf(product.getProduct().getTimeStart()) + "m-" + String.valueOf(product.getProduct().getTimeFinish().toString()) + "m");


                    shopName.setText(product.getShop().getName());
                    shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(product.getShop().getAvatar()));

                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(quantity > 1) {
                                quantity -= 1 ;
                            }
                            else {
                                quantity = 1 ;
                            }
                            quan.setText(String.valueOf(quantity));
                        }
                    });

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(quantity < product.getProduct().getStock()) {
                                quantity +=1;
                            }
                            else {
                                quantity = product.getProduct().getStock();
                            }
                            quan.setText(String.valueOf(quantity));
                        }
                    });

                    Button addToCartBtn = findViewById(R.id.add_to_cart_btn);

                    addToCartBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Gson gson = new Gson();
                            SharedPreferences mPrefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                            String json = mPrefs.getString("user", "");
                            User user = gson.fromJson(json, User.class);

                            if(quan.getText().toString().isEmpty()) {
                                quantity = 0;
                            }
                            else quantity = Integer.parseInt(quan.getText().toString());

                            if(quantity <=0 || quantity > product.getProduct().getStock()) {
                                Toast.makeText(ProductDetail.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                addToCart(user.getId(), pid, quantity);
                            }

                        }
                    });

                    shopLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent shopDetail = new Intent(ProductDetail.this, ShopDetailActivity.class);
                            shopDetail.putExtra("sid", product.getShop().getSid());
                            startActivity(shopDetail);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetProductInfo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void addToCart(int uid, int pid, int quantity) {
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
                        ShowDialogDeleteCart();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    Toast.makeText(ProductDetail.this, response.body().isAdded() ? "Successfully" : "Failed on adding this item to cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void loadListReViewByRate(int pid, int rate, int page) {
        pool = new Pool();

        Call<GetProductReviewRes> getReview = pool.getApiCallUserReview().getReviewOfProduct(pid, rate, page, 3) ;
        getReview.enqueue(new Callback<GetProductReviewRes>() {
            @Override
            public void onResponse(Call<GetProductReviewRes> call, Response<GetProductReviewRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(ProductDetail.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    if(response.body().getReviews() != null) {
                        ArrayList<GetProductReviewRes.ReviewWithImage> list = response.body().getReviews();
                        adapter = new ReviewListAdapter(list, ProductDetail.this);
                        reviewListView.setAdapter(adapter);
                        if(!list.isEmpty()) {
                            reviewLayout.removeView(nothing);
                        }
                        if(list.size() > 2) {
                            seeAllBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductReviewRes> call, Throwable t) {
                System.out.println("fail to fetch API");
            }
        });
    }

    private void ShowDialogDeleteCart() {
        dialog = new DialogDeleteCart(ProductDetail.this, pid, quantity);
        dialog.show(getSupportFragmentManager(), "dialog_delete_cart");
    }
}