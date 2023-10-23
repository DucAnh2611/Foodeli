package com.example.foodeli.Activities.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.OrderStatus.OrderStatusActivity;
import com.example.foodeli.Activities.SelectAddress.SelectAddressActivity;
import com.example.foodeli.Activities.SelectPayment.SelectPaymentActivity;
import com.example.foodeli.Activities.SelectVoucher.SelectVoucherActivity;
import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.GetCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Body.PlaceOrderBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.PlaceOrderRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body.CheckCanUseVoucher;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.CheckVoucherRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartRecyclerViewAdapter.OnItemUpdate{

    private static final int ADDRESS_REQUEST_CODE =1;
    private static final int VOUCHER_REQUEST_CODE =2;
    private static final int PAYMENT_REQUEST_CODE =3;
    private HomeViewModel homeViewModel;
    private ArrayList<GetCartRes.ProductWithImage> list;
    private Pool pool = new Pool();
    public double discountVal, shippingVal, taxVal, totalVal;
    private int aid=0;
    private int vid=0;
    private int ckid=0, mid = 0, ckicon = R.drawable.wallet_non_select;
    private String aname = "", vcode = "", cknum="";
    private TextView addressText, voucherText, paymentText;
    RelativeLayout addressSelect, voucherSelect, paymentSelect;
    private ImageView addressIcon, voucherIcon, paymentIcon;
    private TextView subtotal, shippingFee,  discount, tax, total, empty;
    private CartRecyclerViewAdapter adapter;
    private Button placeOrder;
    private TotalValue totalCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent HomeIntent = getIntent();
        int uid = HomeIntent.getIntExtra("uid", 0);

        RecyclerView recyclerView = findViewById(R.id.cart_all_gv);
        placeOrder = findViewById(R.id.place_orer_btn);
        empty = findViewById(R.id.cart_empty);

        placeOrder.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);

        homeViewModel.getListProductInCart(uid).observe(this, new Observer<ArrayList<GetCartRes.ProductWithImage>>() {
            @Override
            public void onChanged(ArrayList<GetCartRes.ProductWithImage> productWithImages) {
                if(!productWithImages.isEmpty()) {
                    list = new ArrayList<>();
                    list.addAll(productWithImages);

                    recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this,
                            LinearLayoutManager.VERTICAL, false));
                    adapter = new CartRecyclerViewAdapter(list,CartActivity.this, uid);
                    adapter.setOnItemUpdateListener(CartActivity.this);
                    recyclerView.setAdapter(adapter);

                    totalCal = new TotalValue();
                    totalCal.calculateFromList(list);

                    if(productWithImages.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        placeOrder.setVisibility(View.GONE);
                    }
                    else {
                        empty.setVisibility(View.GONE);
                        placeOrder.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    placeOrder.setActivated(false);
                }
            }
        });

        addressSelect = findViewById(R.id.select_address_cart);
        voucherSelect = findViewById(R.id.select_voucher_cart);
        paymentSelect = findViewById(R.id.select_payment_cart);

        addressText = findViewById(R.id.name_address_cart);
        addressIcon = findViewById(R.id.icon_address_cart);

        voucherText = findViewById(R.id.name_voucher_cart);
        voucherIcon = findViewById(R.id.icon_voucher_cart);

        paymentText = findViewById(R.id.name_payment_cart);
        paymentIcon = findViewById(R.id.icon_payment_cart);


        defaultAdd();
        defaultVoucher();
        defaultPayment();

        addressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(CartActivity.this, SelectAddressActivity.class);
                addressIntent.putExtra("aid", aid);
                addressIntent.putExtra("uid", uid);
                addressIntent.putExtra("aname", aname);
                startActivityForResult(addressIntent, ADDRESS_REQUEST_CODE);
            }
        });

        voucherSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherIntent = new Intent(CartActivity.this, SelectVoucherActivity.class);
                voucherIntent.putExtra("uid", uid);
                voucherIntent.putExtra("vid", vid);
                voucherIntent.putExtra("vcode", vcode);
                startActivityForResult(voucherIntent, VOUCHER_REQUEST_CODE);
            }
        });

        paymentSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(CartActivity.this, SelectPaymentActivity.class);
                paymentIntent.putExtra("uid", uid);
                paymentIntent.putExtra("ckid", ckid);
                paymentIntent.putExtra("mid", mid);
                paymentIntent.putExtra("cknum", cknum);
                startActivityForResult(paymentIntent, PAYMENT_REQUEST_CODE);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                SharedPreferences mPrefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String json = mPrefs.getString("user", "");
                User user = gson.fromJson(json, User.class);

                if(list != null || !list.isEmpty()) {
                    if(ckid != 0 && aname != "") {
                        PlaceOrderBody body = new PlaceOrderBody(
                                uid, ckid, vid, cknum.equals("Cash") ? 0 : 1,
                                user.getName(), user.getPhone(), user.getEmail(),
                                aname, totalVal, discountVal, shippingVal, taxVal
                        );
                        Call<PlaceOrderRes> PlaceOrderRes = pool.getApiCallUserOrder().placeOrder(body);

                        PlaceOrderRes.enqueue(new Callback<com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.PlaceOrderRes>() {
                            @Override
                            public void onResponse(Call<PlaceOrderRes> call, Response<PlaceOrderRes> response) {
                                if (!response.isSuccessful()) {
                                    Gson gson = new GsonBuilder().create();
                                    ResponseApi res;
                                    try {
                                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                                        Toast.makeText(CartActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        System.out.println("parse err false");
                                    }
                                } else {
                                    OrderWithState order = response.body().getOrder();

                                    HomeIntent.putExtra("order", order);
                                    setResult(RESULT_OK, HomeIntent);
                                    finish();

                                    Intent orderStatus = new Intent(CartActivity.this, OrderStatusActivity.class);
                                    orderStatus.putExtra("oid", order.getOid());
                                    startActivity(orderStatus);
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceOrderRes> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                    }
                    else {
                        Toast.makeText(CartActivity.this, "Address and Payment is required", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CartActivity.this, "Empty cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void defaultAdd() {
        aid = 0;
        aname = "Select Address";
        addressText.setText(aname);
        addressIcon.setImageResource(R.drawable.address_select);
        addressSelect.setBackgroundResource(R.drawable.custom_input_style);
    }
    private void defaultVoucher() {
        vid = 0;
        vcode = "Apply Voucher";
        voucherText.setText(vcode);
        voucherIcon.setImageResource(R.drawable.voucher_non_select);
        voucherSelect.setBackgroundResource(R.drawable.custom_input_style);
    }
    private void defaultPayment() {
        ckid = 0;
        cknum = "Payment";
        paymentText.setText(cknum);
        paymentIcon.setImageResource(ckicon);
        voucherSelect.setBackgroundResource(R.drawable.custom_input_style);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_REQUEST_CODE) {
                aid = data.getIntExtra("aid", 0);
                aname = data.getStringExtra("aName");
                addressText.setText(aname);

                if(aid != 0) {
                    addressIcon.setImageResource(R.drawable.address_bold);
                    addressSelect.setBackgroundResource(R.drawable.custom_button_style_sec);
                }
                else{
                    addressIcon.setImageResource(R.drawable.address_select);
                    addressSelect.setBackgroundResource(R.drawable.custom_input_style);
                }
            }
            else if (requestCode == VOUCHER_REQUEST_CODE) {
                vid = data.getIntExtra("vid", 0);
                vcode = data.getStringExtra("vcode");
                voucherText.setText(vcode);

                if(vid != 0) {
                    voucherIcon.setImageResource(R.drawable.voucher_select);
                    voucherSelect.setBackgroundResource(R.drawable.custom_button_style_sec);

                    TotalValue updateTotal = new TotalValue();
                    updateTotal.calculateFromList(list);
                }
                else {
                    voucherIcon.setImageResource(R.drawable.voucher_non_select);
                    voucherSelect.setBackgroundResource(R.drawable.custom_input_style);
                }
            }
            else if (requestCode == PAYMENT_REQUEST_CODE) {
                ckid = data.getIntExtra("ckid", 0);
                cknum = data.getStringExtra("cknum");

                ckicon = data.getIntExtra("ckicon", 0);
                mid = data.getIntExtra("mid", 0);
                paymentText.setText(cknum);
                paymentIcon.setImageResource(ckicon);

                if(ckid != 0) {
                    paymentSelect.setBackgroundResource(R.drawable.custom_button_style_sec);
                }
                else {
                    paymentSelect.setBackgroundResource(R.drawable.custom_input_style);
                }
            }
        }
    }

    public void setValueToView(String subtotalValue, String shippingValue, String discountValue, String taxValue, String totalValue) {

        subtotal = findViewById(R.id.cart_subtotal);
        shippingFee = findViewById(R.id.cart_shipping_fee);
        discount = findViewById(R.id.cart_discount);
        tax = findViewById(R.id.cart_tax);
        total = findViewById(R.id.cart_total);


        subtotal.setText("$" + subtotalValue);
        shippingFee.setText("$" + shippingValue);
        discount.setText("$" + discountValue);
        if(!discountValue.equals("0")) {
            discount.setTextColor(getColor(R.color.green_100));
        }
        else {
            discount.setTextColor(getColor(R.color.black));
        }
        tax.setText("$" + taxValue);
        total.setText("$" + totalValue);

    }
    @Override
    public void onItemUpdate() {
        ArrayList<GetCartRes.ProductWithImage> items = adapter.getItem();
        if(items.isEmpty()) {
            defaultAdd();
            defaultVoucher();
            defaultPayment();
        }
        totalCal = new TotalValue();
        totalCal.calculateFromList(items);
    }

    public class TotalValue {
        private double preSubtotal, preShippingFee = 5;

        DecimalFormat df = new DecimalFormat("#.##");

        public void calculateFromList(ArrayList<GetCartRes.ProductWithImage> items) {
            if(items.isEmpty()) {
                preSubtotal = 0;
                setValueToView("0.0", "0.0", "0.0", "0.0", "0.0");
            }
            else {
                preSubtotal = calculateSubtotal(items);
                checkVid();
            }

        }

        private double calculateSubtotal(ArrayList<GetCartRes.ProductWithImage> items) {
            double subPrice = 0;
            for (GetCartRes.ProductWithImage item: items) {
                subPrice += item.getPrice() * Float.parseFloat(String.valueOf(item.getQuantity()));
            }
            return Double.parseDouble(String.valueOf(subPrice));
        }

        private void checkVid() {
            pool = new Pool();

            CheckCanUseVoucher body = new CheckCanUseVoucher(preSubtotal, preShippingFee, vid);
            Call<CheckVoucherRes> checkVoucher = pool.getApiCallUserVoucher().checkCanUseVoucher(body);

            checkVoucher.enqueue(new Callback<CheckVoucherRes>() {
                @Override
                public void onResponse(Call<CheckVoucherRes> call, Response<CheckVoucherRes> response) {
                    if (!response.isSuccessful()) {
                        Gson gson = new GsonBuilder().create();
                        ResponseApi res;
                        try {
                            res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                            Toast.makeText(CartActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            System.out.println("parse err false");
                        }
                        defaultVoucher();
                    }
                    else {
                        CheckVoucherRes.Discount res = response.body().getDiscount();

                        discountVal = res.getDiscount();
                        taxVal = res.getTax();
                        shippingVal = preShippingFee;
                        totalVal = res.getTotal();

                        setValueToView(df.format(preSubtotal), df.format(preShippingFee), df.format(discountVal), df.format(taxVal), df.format(totalVal));

                    }
                }

                @Override
                public void onFailure(Call<CheckVoucherRes> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });

        }

        public TotalValue() {}
    }
}