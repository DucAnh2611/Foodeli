package com.example.foodeli.Fragments.Order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Cart.CartActivity;
import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.Activities.OrderStatus.SelectCancelReasonActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Order;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.ConfirmRes;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderActive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderActive extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int REQUEST_CANCEL = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeViewModel homeViewModel;
    private GridView gridviewOrderActive;
    private OrderActiveGVAdapter adapter;
    private Pool pool;
    private ArrayList<OrderWithState> orderStateActive;
    private User user;
    private LinearLayout emptyLayout;
    private ScrollView loadingView;
    private SupportDate supportDate = new SupportDate();

    public OrderActive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderActive.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderActive newInstance(String param1, String param2) {
        OrderActive fragment = new OrderActive();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order_active, container, false);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        // Inflate the layout for this fragment
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        gridviewOrderActive = view.findViewById(R.id.orderactive_gridview);
        emptyLayout = view.findViewById(R.id.order_gridview_empty);
        loadingView = view.findViewById(R.id.order_gridview_loading);

        gridviewOrderActive.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        homeViewModel.getListOrderActive(user.getId()).observe(getViewLifecycleOwner(), new Observer<ArrayList<OrderWithState>>() {
            @Override
            public void onChanged(ArrayList<OrderWithState> orderWithStates) {
                orderStateActive = orderWithStates;

                adapter= new OrderActiveGVAdapter(orderWithStates, view.getContext());
                adapter.setOnSelectMethodOrder(new OrderActiveGVAdapter.OnSelectMethodOrder() {
                    @Override
                    public void onSelectCancel(OrderWithState order, int position) {
                        navToCancel(order, user.getId(), position);
                    }

                    @Override
                    public void onSelectConfirm(OrderWithState order, int position) {
                        showPopupConfirm(order, user.getId(), position);

                    }
                });
                gridviewOrderActive.setAdapter(adapter);

                if(orderWithStates.isEmpty()) {
                    gridviewOrderActive.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);}
                else {
                    gridviewOrderActive.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
                loadingView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void showPopupConfirm(OrderWithState order, int uid, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_order, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.dialog_confirm_title);
        TextView message = view.findViewById(R.id.dialog_confirm_message);
        AppCompatButton cancelBtn = view.findViewById(R.id.dialog_cancel_btn);
        AppCompatButton confirmBtn = view.findViewById(R.id.dialog_confirm_btn);

        title.setText("Confirm Order");
        message.setText("Are you sure to confirm this order?");
        cancelBtn.setText("Cancel");
        confirmBtn.setText("Confirm");

        AlertDialog alertDialog = builder.create();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool = new Pool();

                Call<ConfirmRes> confirmOrder = pool.getApiCallUserOrder().confirmOrder(uid, order.getOid());
                confirmOrder.enqueue(new Callback<ConfirmRes>() {
                    @Override
                    public void onResponse(Call<ConfirmRes> call, Response<ConfirmRes> response) {
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
                            alertDialog.dismiss();
                            orderStateActive.remove(position);
                            ArrayList<OrderWithState> orderCompleted = homeViewModel.getListOrderCompleted(uid).getValue();

                            Date currentTime = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDateTime = dateFormat.format(currentTime);

                            order.setModified(formattedDateTime);

                            orderCompleted.add(order);

                            homeViewModel.setListOrderCompleted(orderCompleted);
                            homeViewModel.setListOrderActive(orderStateActive);
                        }
                    }

                    @Override
                    public void onFailure(Call<ConfirmRes> call, Throwable t) {
                        System.out.print(t.getMessage());
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void navToCancel(OrderWithState order, int uid, int position) {
        Intent cancelOrder = new Intent(getContext(), SelectCancelReasonActivity.class);
        cancelOrder.putExtra("order", order);
        cancelOrder.putExtra("position", position);
        cancelOrder.putExtra("uid", uid);
        startActivityForResult(cancelOrder, REQUEST_CANCEL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CANCEL) {
                OrderWithState order = (OrderWithState) data.getSerializableExtra("order");
                int position = data.getIntExtra("position", 0);
                int uid = data.getIntExtra("uid", 0);

                orderStateActive =  homeViewModel.getListOrderActive(uid).getValue();
                ArrayList<OrderWithState> listCancel = homeViewModel.getListOrderCancelled(uid).getValue();


                orderStateActive.remove(position);
                order.setModified(supportDate.GetCurrentDateLA());

                if(listCancel != null) {
                    listCancel.add(0, order);
                    homeViewModel.setListOrderCancelled(listCancel);
                }

                homeViewModel.setListOrderActive(orderStateActive);
            }
        }
    }
}