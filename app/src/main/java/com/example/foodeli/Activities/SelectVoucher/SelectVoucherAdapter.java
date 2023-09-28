package com.example.foodeli.Activities.SelectVoucher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodeli.Activities.SelectAddress.SelectAddressAdapter;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;

import java.util.ArrayList;

public class SelectVoucherAdapter extends BaseAdapter {

    private ArrayList<GetAllVoucherRes.VoucherWithRemain> listVoucher;
    private Context context;
    private int tempVid = -1;
    private String tempCode = "";

    private TextView title, desc, quantity;
    private RelativeLayout selectLayout;
    private View innerSelect;

    public SelectVoucherAdapter(ArrayList<GetAllVoucherRes.VoucherWithRemain> listVoucher, int tempVid, String vcode, Context context) {
        this.listVoucher = listVoucher;
        this.tempVid = tempVid;
        this.context = context;
        this.tempCode = vcode;
    }

    @Override
    public int getCount() {
        return listVoucher.size();
    }

    @Override
    public GetAllVoucherRes.VoucherWithRemain getItem(int position) {
        if(!listVoucher.isEmpty()){
            return listVoucher.get(position);

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(!listVoucher.isEmpty()) {
            return listVoucher.get(position).getId();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_select_voucher_gridview, parent, false);
        }
        GetAllVoucherRes.VoucherWithRemain voucher = listVoucher.get(position);

        title = convertView.findViewById(R.id.select_voucher_title);
        desc = convertView.findViewById(R.id.select_voucher_desc);
        quantity = convertView.findViewById(R.id.select_voucher_quantity);
        selectLayout = convertView.findViewById(R.id.select_voucher_layout);
        innerSelect = convertView.findViewById(R.id.select_voucher_inner);

        quantity.setText(String.valueOf(voucher.getRemain()));
        title.setText(voucher.getTitle());
        desc.setText(voucher.getDesc());

        if(tempVid == voucher.getId()) {
            convertView.setBackgroundResource(R.drawable.custom_button_style_sec);
            innerSelect.setVisibility(View.VISIBLE);
        }
        else {
            convertView.setBackgroundResource(R.drawable.custom_input_style);
            innerSelect.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempVid == voucher.getId()) {
                    tempVid = 0;
                    tempCode = "";
                }
                else {
                    tempVid = voucher.getId();
                    tempCode = voucher.getCode();
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public String getTempCode() {
        return tempCode;
    }

    public int getTempVid() {
        return tempVid;
    }
}
