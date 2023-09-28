package com.example.foodeli.Activities.SelectPayment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;

import java.util.ArrayList;

public class SelectPaymentUserMethodAdapter extends BaseAdapter {

    private ArrayList<MethodWithTypeName> list;
    private Context context;

    public SelectPaymentUserMethodAdapter(ArrayList<MethodWithTypeName> list, Context context) {
        this.context = context;
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
        return  list.get(position);
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
        return convertView;
    }
}
