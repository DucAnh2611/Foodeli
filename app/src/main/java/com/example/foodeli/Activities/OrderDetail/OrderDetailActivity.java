package com.example.foodeli.Activities.OrderDetail;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.SelectPayment.ConvertIconMethodIcon;
import com.example.foodeli.Fragments.Order.IdToSerialString;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.OrderState;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderItemsRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private Pool pool;
    private Intent OrderStatusIntent;
    private int oid, uid;
    private OrderTrackRes.OrderInfo orderInfo;
    private OrderTrackRes.OrderWithValue order;
    private OrderTrackRes.PaymentMethod method;
    private ArrayList<OrderItemsRes.OrderItems> orderItems;
    private GridView productGV;
    private ImageView methodIconView;
    private TextView orderId, address, timeFinish, subtotal, shipping, discount, tax, total, methodNum;
    private IdToSerialString idToSerialString = new IdToSerialString();
    private ConvertIconMethodIcon methodIcon = new ConvertIconMethodIcon();
    private SupportDate supportDate = new SupportDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        OrderStatusIntent = getIntent();
        oid = OrderStatusIntent.getIntExtra("oid", 0);
        uid = user.getId();

        methodIconView = findViewById(R.id.order_detail_payment_icon);
        methodNum = findViewById(R.id.order_detail_payment_number);
        productGV = findViewById(R.id.order_detail_gridview_product);
        orderId = findViewById(R.id.order_detail_id);
        address = findViewById(R.id.order_detail_address);
        timeFinish = findViewById(R.id.order_detail_finish);
        subtotal = findViewById(R.id.order_detail_subtotal);
        shipping = findViewById(R.id.order_detail_shipping_fee);
        discount = findViewById(R.id.order_detail_discount);
        tax = findViewById(R.id.order_detail_tax);
        total = findViewById(R.id.order_detail_total);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getOrderInformation(oid);
        getOrderItems(oid, uid);

    }


    private void getOrderInformation(int oid) {

        orderInfo = (OrderTrackRes.OrderInfo) OrderStatusIntent.getSerializableExtra("orderInfo");

        if(orderInfo != null) {
            order = orderInfo.getInfo();
            method = orderInfo.getPaymentMethod();
            ArrayList<OrderTrackRes.OrderTimeLine> orderTimeLines = orderInfo.getOrderTimeLines();

            methodIconView.setImageResource(methodIcon.convertTypeToIcon(method.getMethodType()));
            methodNum.setText(method.getMethodType().equals("Cash") ? "Cash" : method.getNumber());

            orderId.setText(idToSerialString.convertIdToSerialString(oid));
            address.setText(order.getAddress());
            timeFinish.setText(supportDate.ConvertLAtoVN(orderTimeLines.get(orderTimeLines.size()-1).getUpdateAt() ));
            subtotal.setText("$" + order.getSubtotal());
            shipping.setText("$" + order.getShipFee());
            discount.setText("$" + order.getDiscount());
            tax.setText("$" + order.getTax());
            total.setText("$" + order.getTotal());
        }
        else {
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
                        method = response.body().getOrder().getPaymentMethod();
                        ArrayList<OrderTrackRes.OrderTimeLine> orderTimeLines = response.body().getOrder().getOrderTimeLines();

                        methodIconView.setImageResource(methodIcon.convertTypeToIcon(method.getMethodType()));
                        methodNum.setText(method.getMethodType().equals("Cash") ? "Cash" : method.getNumber());

                        orderId.setText(idToSerialString.convertIdToSerialString(oid));
                        address.setText(order.getAddress());
                        timeFinish.setText(orderTimeLines.get(orderTimeLines.size()-1).getUpdateAt());
                        subtotal.setText("$" + order.getSubtotal());
                        shipping.setText("$" + order.getShipFee());
                        discount.setText("$" + order.getDiscount());
                        tax.setText("$" + order.getTax());
                        total.setText("$" + order.getTotal());
                    }
                }

                @Override
                public void onFailure(Call<OrderTrackRes> call, Throwable t) {
                    System.out.print(t.getMessage());
                }
            });
        }

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
                    orderItems = response.body().getItems();
                    OrderDetailProductsAdapter adapter = new OrderDetailProductsAdapter(orderItems, OrderDetailActivity.this);
                    productGV.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<OrderItemsRes> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
    }
}