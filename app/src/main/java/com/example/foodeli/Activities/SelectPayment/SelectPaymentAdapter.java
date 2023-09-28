package com.example.foodeli.Activities.SelectPayment;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Cart.CartActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.General.Response.MethodSupportRes;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.GetAllMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Method;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPaymentAdapter extends BaseAdapter {

    private ArrayList<MethodSupport> listSupport;
    private HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod = new HashMap<>();
    private int itemSelect = -1, uid;
    private boolean load=false;
    private String itemNumber = "", itemType = "";
    private Pool pool;
    private Context context;
    private ImageView iconSupport, isOpenIconSupport;
    private TextView typeSupport;
    private GridView subGridViewUserMethod;
    private RelativeLayout supportSelectLayout;
    private View innerSelect;
    private LinearLayout paymentLayout;
    private HashMap<String, Integer> mapIconSupportList;
    private SelectPaymentUserMethodAdapter adapter;

    public SelectPaymentAdapter(ArrayList<MethodSupport> listSupport, int uid, int itemSelect,
                                String itemNumber, String itemType,
                                HashMap<String, Integer> mapIconSupportList, Context context) {
        this.listSupport = listSupport;
        this.itemSelect = itemSelect;
        this.itemNumber = itemNumber;
        this.itemType = itemType;
        this.uid = uid;
        this.mapIconSupportList = mapIconSupportList;
        this.context = context;
    }

    private void CallAPIUserHave() {
        pool = new Pool();

        Call<GetAllMethod> allMethodUserCall = pool.getApiCallUserMethod().getAllMethod(uid);
        allMethodUserCall.enqueue(new Callback<GetAllMethod>() {
            @Override
            public void onResponse(Call<GetAllMethod> call, Response<GetAllMethod> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<MethodWithTypeName> listMethodSupport = response.body().getList();

                    for (MethodWithTypeName item: listMethodSupport) {
                        ArrayList<MethodWithTypeName> addedEl = mapListMethod.containsKey(item.getType())
                                ? mapListMethod.get(item.getType())
                                : new ArrayList<>();

                        addedEl.add(item);
                        mapListMethod.put(item.getType(), addedEl);
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetAllMethod> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public int getCount() {
        return listSupport.size();
    }

    @Override
    public MethodSupport getItem(int position) {
        if(listSupport.isEmpty()) {
            return null;
        }
        return listSupport.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listSupport.isEmpty()) {
            return 0;
        }
        return listSupport.get(position).getMid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_select_payment_gridview, parent, false);
        }

        MethodSupport methodSupport = getItem(position);

        iconSupport = convertView.findViewById(R.id.method_support_icon);
        typeSupport = convertView.findViewById(R.id.method_support_name);
        paymentLayout = convertView.findViewById(R.id.select_payment_layout);
        isOpenIconSupport = convertView.findViewById(R.id.method_support_is_select);
        subGridViewUserMethod = convertView.findViewById(R.id.method_support_subgridview);
        supportSelectLayout = convertView.findViewById(R.id.method_support_layout);
        innerSelect = convertView.findViewById(R.id.method_support_inner);

        if(!load) {
            load =true;
            CallAPIUserHave();
        }
        else {
            iconSupport.setImageResource(mapIconSupportList.get(methodSupport.getType()));
            typeSupport.setText(methodSupport.getType());

            if (itemSelect == methodSupport.getMid()) {
                if (methodSupport.getType().equals("Cash")) {
                    innerSelect.setVisibility(View.VISIBLE);
                }
                else {
                    if(mapListMethod.get(methodSupport.getType()) != null) {
                        adapter = new SelectPaymentUserMethodAdapter(mapListMethod.get(methodSupport.getType()), context);
                        subGridViewUserMethod.setAdapter(adapter);
                    }

                    isOpenIconSupport.setImageResource(R.drawable.down_arrow);
                    subGridViewUserMethod.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                }
                paymentLayout.setBackgroundResource(R.drawable.custom_button_style_sec);
            } else {
                if (methodSupport.getType().equals("Cash")) {
                    paymentLayout.removeView(isOpenIconSupport);
                    innerSelect.setVisibility(View.INVISIBLE);
                } else {
                    paymentLayout.removeView(supportSelectLayout);
                    isOpenIconSupport.setImageResource(R.drawable.right_arrow);
                }

                subGridViewUserMethod.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                paymentLayout.setBackgroundResource(R.drawable.custom_input_style);
            }
            paymentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemSelect == methodSupport.getMid() ) {
                        itemSelect = 0;
                        itemType = "";
                    }
                    else {
                        itemSelect = methodSupport.getMid();
                        itemType = methodSupport.getType();
                    }
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

}
