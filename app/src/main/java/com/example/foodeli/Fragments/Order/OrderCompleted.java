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

import com.example.foodeli.Activities.Home.HomeViewModel;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderCompleted#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCompleted extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeViewModel homeViewModel;
    private GridView orderCompletedGV;
    private OrderCompletedGVAdapter adapter;

    public OrderCompleted() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderCompleted.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderCompleted newInstance(String param1, String param2) {
        OrderCompleted fragment = new OrderCompleted();
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
        View view = inflater.inflate(R.layout.fragment_order_completed, container, false);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        User user = gson.fromJson(json, User.class);

        // Inflate the layout for this fragment
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        orderCompletedGV = view.findViewById(R.id.ordercompleted_gridview);

        homeViewModel.getListOrderCompleted(user.getId()).observe(getViewLifecycleOwner(), new Observer<ArrayList<OrderWithState>>() {
            @Override
            public void onChanged(ArrayList<OrderWithState> orderWithStates) {
                adapter = new OrderCompletedGVAdapter(orderWithStates, view.getContext());
                orderCompletedGV.setAdapter(adapter);
            }
        });
        return view;
    }
}