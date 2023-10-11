package com.example.foodeli.Activities.Voucher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;

import java.util.ArrayList;

public class UserVoucherGridviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GetAllVoucherRes.VoucherWithRemain> listVou;
    private TextView quan, title, desc, code;

    public UserVoucherGridviewAdapter(ArrayList<GetAllVoucherRes.VoucherWithRemain> listVou, Context context) {
        this.context = context;
        this.listVou = listVou;
    }

    @Override
    public int getCount() {
        return listVou.size();
    }

    @Override
    public GetAllVoucherRes.VoucherWithRemain getItem(int position) {
        if(listVou.isEmpty()) {
            return null;
        }
        return listVou.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(listVou.isEmpty()) {
            return 0;
        }
        return listVou.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_user_voucher_gridview, parent, false);
        }

        GetAllVoucherRes.VoucherWithRemain item = getItem(position);

        code = convertView.findViewById(R.id.user_voucher_code);
        title = convertView.findViewById(R.id.user_voucher_title);
        desc = convertView.findViewById(R.id.user_voucher_desc);
        quan = convertView.findViewById(R.id.user_voucher_quantity);

        code.setText(item.getCode());
        title.setText(item.getTitle());
        desc.setText(item.getDesc());
        quan.setText(String.valueOf(item.getRemain()));

        return convertView;
    }

    public void setListVou(ArrayList<GetAllVoucherRes.VoucherWithRemain> listVou) {
        this.listVou = listVou;
        notifyDataSetChanged();
    }
}
