package com.example.foodeli.Fragments.ShopOrder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.Toast;

import com.example.foodeli.Activities.ShopManage.ShopManageViewModel;
import com.example.foodeli.Fragments.ShopProduct.DynamicHeightGridView;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucherAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.UpdateItemRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.OrderWithState;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
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
 * Use the {@link ShopOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopOrder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SID = "arg_sid";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int sid;
    private View view;
    private GridView orderGridView;
    private ShopOrderAdapter adapter;
    private ShopManageViewModel shopManageViewModel;
    private User user;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OnAdjustOrder onAdjustOrder;

    public ShopOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sid shopId .
     * @return A new instance of fragment ShopOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopOrder newInstance(int sid) {
        ShopOrder fragment = new ShopOrder();
        Bundle args = new Bundle();
        args.putInt(ARG_SID, sid);
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
        view = inflater.inflate(R.layout.fragment_shop_order, container, false);
        sid = getArguments().getInt(ARG_SID);

        orderGridView = view.findViewById(R.id.item_shop_order_gridview);
        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        shopManageViewModel = new ViewModelProvider(getActivity()).get(ShopManageViewModel.class);

        adapter = new ShopOrderAdapter(getContext(), new ArrayList<>());
        orderGridView.setAdapter(adapter);

        shopManageViewModel.getListOrderShop(sid).observe(getViewLifecycleOwner(), new Observer<ArrayList<OrderWithState>>() {
            @Override
            public void onChanged(ArrayList<OrderWithState> orderWithValues) {
                adapter = new ShopOrderAdapter(getContext(), orderWithValues);
                orderGridView.setAdapter(adapter);

                adapter.setOnMethodOrderShop(new ShopOrderAdapter.OnMethodOrderShop() {
                    @Override
                    public void onChangeStated(int position) {
                        updateOrderAtIndex(position);
                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public String convertIdToState(int id) {
        switch(id) {
            case 2:
                return "Cooking";
            case 3:
                return "Delivering";
            case 4:
                return "Arrived";
            default:
                return "";
        }
    }

    public void updateOrderAtIndex(int position) {

        ArrayList<OrderWithState> oldList = shopManageViewModel.getListOrderShop(position).getValue();
        OrderWithState order = oldList.get(position);

        Pool pool = new Pool();

        Call<UpdateItemRes> updateOrder = pool.getApiCallShopOrder().updateOrder(sid, user.getId(), order.getOid(), order.getStateId() +1);

        updateOrder.enqueue(new Callback<UpdateItemRes>() {
            @Override
            public void onResponse(Call<UpdateItemRes> call, Response<UpdateItemRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                        Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    oldList.remove(position);

                    Date currentTime = new Date();
                    String formattedDateTime = dateFormat.format(currentTime);

                    order.setModified(formattedDateTime);
                    order.setStateId(order.getStateId() + 1);
                    order.setState(convertIdToState(order.getStateId()));

                    oldList.add(0, order);
                    adapter.setList(oldList);
                    shopManageViewModel.setListOrderShop(oldList);

                    onAdjustOrder.onAdjust();
                }
            }

            @Override
            public void onFailure(Call<UpdateItemRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public interface OnAdjustOrder {
        void onAdjust();
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setOnAdjustOrder(OnAdjustOrder onAdjustOrder) {
        this.onAdjustOrder = onAdjustOrder;
    }
}