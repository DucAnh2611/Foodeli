package com.example.foodeli.Fragments.Order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCancelled#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCancelled extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OrderCancelledGVAdapter adapter;
    private HomeViewModel homeViewModel;
    private GridView orderCancelledGV;
    private LinearLayout emptyLayout;
    private ScrollView loadingView;
    public OrderCancelled() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderCancelled.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderCancelled newInstance(String param1, String param2) {
        OrderCancelled fragment = new OrderCancelled();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_cancelled, container, false);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        // Inflate the layout for this fragment
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        orderCancelledGV = view.findViewById(R.id.ordercancelled_gridview);
        emptyLayout = view.findViewById(R.id.order_gridview_empty);
        loadingView = view.findViewById(R.id.order_gridview_loading);

        orderCancelledGV.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        homeViewModel.getListOrderCancelled(user.getId()).observe(getViewLifecycleOwner(), new Observer<ArrayList<OrderWithState>>() {
            @Override
            public void onChanged(ArrayList<OrderWithState> orderWithStates) {
                adapter = new OrderCancelledGVAdapter(orderWithStates, view.getContext());
                orderCancelledGV.setAdapter(adapter);

                if(orderWithStates.isEmpty()) {
                    orderCancelledGV.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);}
                else {
                    orderCancelledGV.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
                loadingView.setVisibility(View.GONE);
            }
        });

        return view;
    }
}