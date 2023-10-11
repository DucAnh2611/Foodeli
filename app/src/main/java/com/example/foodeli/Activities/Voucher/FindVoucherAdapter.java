package com.example.foodeli.Activities.Voucher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;

import java.util.ArrayList;

public class FindVoucherAdapter extends BaseAdapter {

    private ArrayList<com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher> list;
    private Context context;
    private TextView code, title, desc, limit;
    private AppCompatButton saveBtn;
    private OnSaveVoucher onSaveVoucher;

    public FindVoucherAdapter(ArrayList<com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Voucher getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.items_find_voucher_gridview, parent, false);
        }

        Voucher item = getItem(position);

        title = convertView.findViewById(R.id.find_voucher_title);
        code = convertView.findViewById(R.id.find_voucher_code);
        desc = convertView.findViewById(R.id.find_voucher_desc);
        limit = convertView.findViewById(R.id.find_voucher_limit);
        saveBtn = convertView.findViewById(R.id.find_voucher_save);

        title.setText(item.getTitle());
        code.setText(item.getCode());
        desc.setText(item.getDesc());
        limit.setText(String.valueOf(item.getLimit()));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveVoucher.onSaveVoucher(item.getId());
            }
        });

        return convertView;
    }

    public void setList(ArrayList<Voucher> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnSaveVoucher {
        void onSaveVoucher(int vid);
    }

    public void setOnSaveVoucher(OnSaveVoucher onSaveVoucher) {
        this.onSaveVoucher = onSaveVoucher;
    }
}
