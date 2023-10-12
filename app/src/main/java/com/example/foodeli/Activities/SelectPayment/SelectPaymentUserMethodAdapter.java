package com.example.foodeli.Activities.SelectPayment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.Method.Method;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.R;

import java.util.ArrayList;

public class SelectPaymentUserMethodAdapter extends BaseAdapter {

    private ArrayList<MethodWithTypeName> list;
    private Context context;
    private int tempMethodId;
    private String tempMethodNumber;
    private TextView number, expireDate;
    private RelativeLayout selectLayout;
    private View selectInner;
    private OnClickItem onClickItem;
    public SelectPaymentUserMethodAdapter(ArrayList<MethodWithTypeName> list, int ckSelect, Context context) {
        this.context = context;
        this.tempMethodId = ckSelect;
        this.list = list;
    };

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MethodWithTypeName getItem(int position) {
        if(list.isEmpty()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(list.isEmpty()) {
            return 0;
        }
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_select_payment_subgridview, parent, false);
        }

        MethodWithTypeName method = getItem(position);

        number = convertView.findViewById(R.id.select_payment_subgridview_number);
        expireDate = convertView.findViewById(R.id.select_payment_subgridview_expire);
        selectLayout = convertView.findViewById(R.id.select_payment_subgridview_layout);
        selectInner = convertView.findViewById(R.id.select_payment_subgridview_inner);

        String stringDate = method.getExpired();
        String[] splitDate = stringDate.split("-");

        int year = Integer.parseInt(splitDate[0].substring(2));
        int month = Integer.parseInt(splitDate[1]);

        number.setText(method.getNumber());
        expireDate.setText((month < 10 ? "0" + month  : String.valueOf(month)) + "/" + year);

        if(tempMethodId == method.getId()) {
            selectInner.setVisibility(View.VISIBLE);
        }
        else {
            selectInner.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempMethodId == method.getId()) {
                    tempMethodId = 0;
                    tempMethodNumber = "Payment";
                }
                else {
                    tempMethodId = method.getId();
                    tempMethodNumber = method.getNumber();
                }
                onClickItem.onClickItem();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public String getTempMethodNumber() {
        return tempMethodNumber;
    }

    public int getTempMethodId() {
        return tempMethodId;
    }

    public interface OnClickItem {
        void onClickItem();
    }

    public void itemClick(OnClickItem onClickItem) {this.onClickItem = onClickItem;}

}
