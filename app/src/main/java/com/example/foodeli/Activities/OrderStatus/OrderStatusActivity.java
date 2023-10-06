package com.example.foodeli.Activities.OrderStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.OrderDetail.OrderDetailActivity;
import com.example.foodeli.Activities.SelectPayment.ConvertIconMethodIcon;
import com.example.foodeli.Fragments.Order.IdToSerialString;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.OrderState;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {

    private Pool pool;
    private int oid, uid;
    private Intent orderDetailIntent;
    private OrderTrackRes.OrderWithValue order;
    private HomeViewModel homeViewModel;
    private GridView timelineGV;
    private ImageView methodIconView;
    private TextView orderId, address, estimate, subtotal, shipping, discount, tax, total, methodNum;
    private Button orderDetailBtn;
    private OrderStatusStateAdapter stateAdapter;
    private IdToSerialString idToSerialString = new IdToSerialString();
    private ConvertIconMethodIcon methodIcon = new ConvertIconMethodIcon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        methodIconView = findViewById(R.id.order_status_payment_icon);
        methodNum = findViewById(R.id.order_status_payment_number);
        timelineGV = findViewById(R.id.order_status_gridview_timeline);
        orderId = findViewById(R.id.order_status_id);
        address = findViewById(R.id.order_status_address);
        estimate = findViewById(R.id.order_status_estimate);
        subtotal = findViewById(R.id.order_status_subtotal);
        shipping = findViewById(R.id.order_status_shipping_fee);
        discount = findViewById(R.id.order_status_discount);
        tax = findViewById(R.id.order_status_tax);
        total = findViewById(R.id.order_status_total);
        orderDetailBtn = findViewById(R.id.order_status_btn_to_detail);

        orderDetailIntent = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);
        Intent OrderIntent = getIntent();
        int t_oid = OrderIntent.getIntExtra("oid", 0);
        oid = t_oid;
        uid = user.getId();

        getOrderInformation(oid, uid);

        orderDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetailIntent.putExtra("oid", oid);
                startActivity(orderDetailIntent);
            }
        });

    }

    private void getOrderInformation(int oid, int uid) {
        pool = new Pool();

        Call<OrderTrackRes> getOrderInfo = pool.getApiCallUserOrder().trackingOrder(oid, uid);

        getOrderInfo.enqueue(new Callback<OrderTrackRes>() {
            @Override
            public void onResponse(Call<OrderTrackRes> call, Response<OrderTrackRes> response) {
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
                    order = response.body().getOrder().getInfo();
                    ArrayList<OrderTrackRes.OrderTimeLine> dateUpdate = response.body().getOrder().getOrderTimeLines();
                    OrderTrackRes.PaymentMethod method = response.body().getOrder().getPaymentMethod();

                    orderDetailIntent.putExtra("orderInfo", response.body().getOrder());

                    homeViewModel.getOrderState().observe(OrderStatusActivity.this, new Observer<ArrayList<OrderState>>() {
                        @Override
                        public void onChanged(ArrayList<OrderState> orderStates) {
                            stateAdapter = new OrderStatusStateAdapter(orderStates, order.getStateId() ,dateUpdate, OrderStatusActivity.this );
                            timelineGV.setAdapter(stateAdapter);

                            orderId.setText(idToSerialString.convertIdToSerialString(oid));
                            address.setText(order.getAddress());
                            estimate.setText(order.getEstimate());
                            methodIconView.setImageResource(methodIcon.convertTypeToIcon(method.getMethodType()));
                            methodNum.setText(method.getMethodType().equals("Cash") ? "Cash" : method.getNumber());
                            subtotal.setText("$" + order.getSubtotal());
                            shipping.setText("$" + order.getShipFee());
                            discount.setText("$" + order.getDiscount());
                            tax.setText("$" + order.getTax());
                            total.setText("$" + order.getTotal());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<OrderTrackRes> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });

    }

}