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

public class SelectPaymentAdapter extends BaseAdapter implements SelectPaymentUserMethodAdapter.OnClickItem{

    private ArrayList<MethodSupport> listSupport;
    private HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod;
    private int itemSelect = -1, uid, ckSelect = -1;
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

    public SelectPaymentAdapter(ArrayList<MethodSupport> listSupport, int uid, int itemSelect, int ckSelect,
                                HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod, String itemNumber,
                                HashMap<String, Integer> mapIconSupportList, Context context) {
        this.listSupport = listSupport;
        this.mapListMethod = mapListMethod;
        this.itemSelect = itemSelect;
        this.itemNumber = itemNumber;
        this.ckSelect = ckSelect;
        this.uid = uid;
        this.mapIconSupportList = mapIconSupportList;
        this.context = context;
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

        convertView = LayoutInflater.from(context).inflate(R.layout.items_select_payment_gridview, parent, false);
        MethodSupport methodSupport = getItem(position);

        iconSupport = convertView.findViewById(R.id.method_support_icon);
        typeSupport = convertView.findViewById(R.id.method_support_name);
        paymentLayout = convertView.findViewById(R.id.select_payment_layout);
        isOpenIconSupport = convertView.findViewById(R.id.method_support_is_select);
        subGridViewUserMethod = convertView.findViewById(R.id.method_support_subgridview);
        supportSelectLayout = convertView.findViewById(R.id.method_support_layout);
        innerSelect = convertView.findViewById(R.id.method_support_inner);


        iconSupport.setImageResource(mapIconSupportList.get(methodSupport.getType()));
        typeSupport.setText(methodSupport.getType());

        if (itemSelect == methodSupport.getMid()) {
            if (methodSupport.getType().equals("Cash")) {
                innerSelect.setVisibility(View.VISIBLE);
                isOpenIconSupport.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            }
            else {
                supportSelectLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

                if (mapListMethod.get(methodSupport.getType()) != null) {
                    adapter = new SelectPaymentUserMethodAdapter(mapListMethod.get(methodSupport.getType()), ckSelect, context);
                    adapter.itemClick(this);
                    subGridViewUserMethod.setAdapter(adapter);

                    int desiredHeight = (int) (mapListMethod.get(methodSupport.getType()) == null ? 0 : mapListMethod.get(methodSupport.getType()).size() *
                            (context.getResources().getDimension(R.dimen.item_in_method) +
                                    context.getResources().getDimension(R.dimen.item_gap_in_method)) + subGridViewUserMethod.getPaddingTop() + subGridViewUserMethod.getPaddingBottom());
                    subGridViewUserMethod.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            desiredHeight
                    ));
                }

                isOpenIconSupport.setImageResource(R.drawable.down_arrow);
            }
            paymentLayout.setBackgroundResource(R.drawable.custom_button_style_sec);
        }
        else {
            if (methodSupport.getType().equals("Cash")) {
                isOpenIconSupport.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                innerSelect.setVisibility(View.INVISIBLE);
            } else {
                supportSelectLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                isOpenIconSupport.setImageResource(R.drawable.right_arrow);
            }

            subGridViewUserMethod.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            paymentLayout.setBackgroundResource(R.drawable.custom_input_style);
        }
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelect == methodSupport.getMid()) {
                    itemSelect = 0;
                    itemType = "";
                    itemNumber = "Payment";
                    if (methodSupport.getType().equals("Cash")) {
                        ckSelect = 0;
                    }
                } else {
                    itemSelect = methodSupport.getMid();
                    itemType = methodSupport.getType();
                    if (methodSupport.getType().equals("Cash")) {
                        itemNumber = "Cash";
                        ckSelect = mapListMethod.get("Cash").get(0).getId();
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public int getItemSelect() {
        return itemSelect;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public int getCkSelect() {
        return ckSelect;
    }

    @Override
    public void onClickItem() {
        itemNumber = adapter.getTempMethodNumber();
        ckSelect = adapter.getTempMethodId();
    }

    public HashMap<String, ArrayList<MethodWithTypeName>> getMapListMethod() {
        return mapListMethod;
    }

    public void setMapListMethod(HashMap<String, ArrayList<MethodWithTypeName>> mapListMethod) {
        this.mapListMethod = mapListMethod;
        notifyDataSetChanged();
    }
}


